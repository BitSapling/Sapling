package com.github.bitsapling.sapling.service;

import com.github.bitsapling.sapling.entity.Peer;
import com.github.bitsapling.sapling.entity.Torrent;
import com.github.bitsapling.sapling.entity.User;
import com.github.bitsapling.sapling.type.AnnounceEventType;
import com.github.bitsapling.sapling.util.ExecutorUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

@Service
@Slf4j
@Transactional
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
    private AnnouncePerformanceMonitorService monitorService;

    public AnnounceService() {
        Thread thread = new Thread(() -> {
            try {
                handleTasks();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void schedule(@NotNull AnnounceTask announceTask) {
        try {
            taskQueue.add(announceTask);
        } catch (IllegalStateException exception) {
            log.error("Announce service is busy! Check your server database!", exception);
        }
    }

    public void handleTasks() throws InterruptedException {
        while (true) {
            AnnounceTask task = taskQueue.take();
            executor.getAnnounceExecutor().submit(() -> {
                try {
                    long start = System.nanoTime();
                    handleTask(task);
                    monitorService.recordJobStats(System.nanoTime() - start);
                } catch (Exception e) {
                    log.error("Error handling task: {}", task, e);
                }
            });
        }
    }

    public void handleTask(AnnounceTask task) throws NoSuchElementException {
        // Multi-threaded
        User user = userService.getUser(task.userId());
        if (user == null) throw new IllegalStateException("User not exists anymore");
        Torrent torrent = torrentService.getTorrent(task.torrentId());
        if (torrent == null) throw new IllegalStateException("Torrent not exists anymore");
        // Register torrent into peers
        Peer peer = peerService.getPeer(task.ip(), task.port(), task.infoHash());
        if (peer == null) {
            peer = createNewPeer(task);
        }
        long lastUploaded = peer.getUploaded();
        long lastDownload = peer.getDownloaded();
        long uploadedOffset = task.uploaded() - lastUploaded;
        long downloadedOffset = task.downloaded() - lastDownload;
        Timestamp lastUpdateAt = torrent.getUpdatedAt();
        if (uploadedOffset < 0) uploadedOffset = task.uploaded();
        if (downloadedOffset < 0) downloadedOffset = task.downloaded();

        peer.setUploaded(task.uploaded() + uploadedOffset);
        peer.setDownloaded(task.downloaded() + downloadedOffset);
        peer.setLeft(task.left());
        peer.setSeeder(task.left() == 0);
        peer.setUpdateAt(Timestamp.from(Instant.now()));
        peer.setSeedingTime(peer.getSeedingTime() + (Instant.now().toEpochMilli() - lastUpdateAt.toInstant().toEpochMilli()));
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
        log.info("Updated user {}'s data: uploaded {}, downloaded {} with original data: actual-uploaded {}, actual-downloaded {} for ip address {} and port {}",
                user.getUsername(), promotionUploadOffset, promotionDownloadOffset, uploadedOffset, downloadedOffset,
                task.ip(), task.port());
        if (task.event() == AnnounceEventType.STOPPED) {
            if (peer.getId() != 0) {
                peerService.delete(peer);
            }
        }
    }


    @NotNull
    private Peer createNewPeer(AnnounceTask task) {
        log.debug("Creating a new peer for: {}", task.infoHash());
        // TorrentEntity torrent = torrentRepository.findByInfoHash(task.infoHash()).orElseThrow();
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
                task.passKey(),
                Timestamp.from(Instant.now()),
                0
        );
    }

    public BlockingDeque<AnnounceTask> getTaskQueue() {
        return taskQueue;
    }

    public record AnnounceTask(
            @NotNull String ip, int port, @NotNull String infoHash, @NotNull String peerId,
            long uploaded, long downloaded, long left, @NotNull AnnounceEventType event,
            int numWant, long userId, boolean compact, boolean noPeerId,
            boolean supportCrypto, int redundant, String userAgent, String passKey, long torrentId
    ) {

    }
}
