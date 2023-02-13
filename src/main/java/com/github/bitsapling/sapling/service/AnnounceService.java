package com.github.bitsapling.sapling.service;

import com.github.bitsapling.sapling.entity.Peer;
import com.github.bitsapling.sapling.entity.Torrent;
import com.github.bitsapling.sapling.entity.TransferHistory;
import com.github.bitsapling.sapling.entity.User;
import com.github.bitsapling.sapling.exception.AnnounceBusyException;
import com.github.bitsapling.sapling.type.AnnounceEventType;
import com.github.bitsapling.sapling.util.ExecutorUtil;
import com.github.bitsapling.sapling.util.HibernateSessionUtil;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

@Service
@Slf4j
public class AnnounceService {
    private final BlockingDeque<AnnounceTask> taskQueue = new LinkedBlockingDeque<>(40960);
    @Autowired
    private ExecutorUtil executor;
    @Autowired
    private UserService userService;
    @Autowired
    private PeerService peerService;
    @Autowired
    private TorrentService torrentService;
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    private AnnouncePerformanceMonitorService monitorService;
    @Autowired
    private TransferHistoryService transferHistoryService;
    @Autowired
    private HibernateSessionUtil sessionUtil;

    public AnnounceService() {
        Thread announceHandleThread = new Thread(() -> {
            while (true) {
                try {
                    AnnounceTask announceTask = taskQueue.take();
                    boolean participate = sessionUtil.bindToThread();
                    try {
                        long start = System.nanoTime();
                        handleTask(announceTask);
                        monitorService.recordJobStats(System.nanoTime() - start);
                    } catch (Exception e) {
                        log.error("Error handling task: {}", announceTask, e);
                    } finally {
                        sessionUtil.closeFromThread(participate);
                    }
                } catch (InterruptedException e) {
                    log.error("Announce handling thread interrupted", e);
                }
            }
        });
        announceHandleThread.setName("Announce Handling");
        announceHandleThread.setDaemon(true);
        announceHandleThread.start();
    }

    public void schedule(@NotNull AnnounceTask announceTask) throws AnnounceBusyException {
        if (!this.taskQueue.offer(announceTask))
            throw new AnnounceBusyException();
    }

    @Transactional
    void handleTask(AnnounceTask task) throws NoSuchElementException {
        // Multi-threaded
        User user = userService.getUser(task.userId());
        if (user == null) throw new IllegalStateException("User not exists anymore");
        Torrent torrent = torrentService.getTorrent(task.torrentId());
        if (torrent == null) throw new IllegalStateException("Torrent not exists anymore");
        // Register torrent into peers
        Peer peer = peerService.getPeer(task.ip(), task.port(), task.infoHash());
        if (peer == null) {
            peer = createNewPeer(task, user);
        }
        long lastUploaded = peer.getUploaded();
        long lastDownload = peer.getDownloaded();
        long uploadedOffset = task.uploaded() - lastUploaded;
        long downloadedOffset = task.downloaded() - lastDownload;
        Timestamp lastUpdateAt = torrent.getUpdatedAt();
        if (uploadedOffset < 0) uploadedOffset = task.uploaded();
        if (downloadedOffset < 0) downloadedOffset = task.downloaded();
        long announceInterval = Instant.now().toEpochMilli() - lastUpdateAt.toInstant().toEpochMilli();
        peer.setUploaded(task.uploaded() + uploadedOffset);
        peer.setDownloaded(task.downloaded() + downloadedOffset);
        peer.setLeft(task.left());
        peer.setSeeder(task.left() == 0);
        peer.setUpdateAt(Timestamp.from(Instant.now()));
        peer.setSeedingTime(peer.getSeedingTime() + (Instant.now().toEpochMilli() - lastUpdateAt.toInstant().toEpochMilli()));
        peer.setPartialSeeder(task.event() == AnnounceEventType.PAUSED);
        // Update user peer speed
        long bytesPerSecondUploading = uploadedOffset / (announceInterval / 1000);
        long bytesPerSecondDownloading = downloadedOffset / (announceInterval / 1000);
        peer.setUploadSpeed(bytesPerSecondUploading);
        peer.setDownloadSpeed(bytesPerSecondDownloading);
        peer = peerService.save(peer);
        // Update real user data
        user.setRealDownloaded(user.getRealDownloaded() + lastDownload);
        user.setRealUploaded(user.getRealUploaded() + lastUploaded);
        // Apply user promotion policy
        long promotionUploadOffset = (long) user.getGroup().getPromotionPolicy().applyUploadRatio(lastDownload);
        long promotionDownloadOffset = (long) user.getGroup().getPromotionPolicy().applyDownloadRatio(lastUploaded);
        // Apply torrent promotion policy
        promotionUploadOffset = (long) torrent.getPromotionPolicy().applyUploadRatio(promotionUploadOffset);
        promotionDownloadOffset = (long) torrent.getPromotionPolicy().applyDownloadRatio(promotionDownloadOffset);
        user.setUploaded(user.getUploaded() + promotionUploadOffset);
        user.setDownloaded(user.getDownloaded() + promotionDownloadOffset);
        user.setSeedingTime(user.getSeedingTime() + (Instant.now().toEpochMilli() - lastUpdateAt.toInstant().toEpochMilli()));
        user = userService.save(user);
        TransferHistory transferHistory = transferHistoryService.getTransferHistory(user, torrent);
        if (transferHistory != null) {
            long torrentLeft = transferHistory.getLeft();
            if (torrentLeft != 0 && task.left() == 0) {
                transferHistory.setHaveCompleteHistory(true);
            }
            transferHistory.setUpdatedAt(Timestamp.from(Instant.now()));
            transferHistory.setLeft(task.left());
            transferHistory.setUploaded(transferHistory.getUploaded() + promotionUploadOffset);
            transferHistory.setDownloaded(transferHistory.getDownloaded() + promotionDownloadOffset);
            transferHistory.setActualUploaded(transferHistory.getActualUploaded() + uploadedOffset);
            transferHistory.setActualDownloaded(transferHistory.getActualDownloaded() + downloadedOffset);
            transferHistory.setUploadSpeed(bytesPerSecondUploading);
            transferHistory.setDownloadSpeed(bytesPerSecondDownloading);
        } else {
            transferHistory = new TransferHistory(0, user, torrent,
                    task.left(), Timestamp.from(Instant.now()),
                    Timestamp.from(Instant.now()),
                    promotionUploadOffset, promotionDownloadOffset, uploadedOffset, downloadedOffset,
                    bytesPerSecondUploading, bytesPerSecondDownloading, task.event(), false);
        }
        transferHistoryService.save(transferHistory);
        torrentService.save(torrent);
        if (task.event() == AnnounceEventType.STOPPED) {
            if (peer.getId() != 0) {
                peerService.delete(peer);
            }
        }

    }


    @NotNull
    private Peer createNewPeer(AnnounceTask task, User user) {
        return new Peer(
                0,
                task.ip(),
                task.port(),
                task.infoHash(),
                task.peerId(),
                task.userAgent(),
                task.uploaded(),
                task.downloaded(),
                task.left(),
                task.left() == 0,
                task.event() == AnnounceEventType.PAUSED,
                task.passKey(),
                Timestamp.from(Instant.now()),
                0, 0,
                0,
                user
        );
    }

    public record AnnounceTask(
            @NotNull String ip, int port, @NotNull String infoHash, @NotNull String peerId,
            long uploaded, long downloaded, long left, @NotNull AnnounceEventType event,
            int numWant, long userId, boolean compact, boolean noPeerId,
            boolean supportCrypto, int redundant, String userAgent, String passKey, long torrentId
    ) {

    }
}
