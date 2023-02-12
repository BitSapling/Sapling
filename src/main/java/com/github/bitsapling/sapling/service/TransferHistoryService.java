package com.github.bitsapling.sapling.service;

import com.github.bitsapling.sapling.entity.Torrent;
import com.github.bitsapling.sapling.entity.TransferHistory;
import com.github.bitsapling.sapling.entity.User;
import com.github.bitsapling.sapling.repository.TransferHistoryRepository;
import com.github.bitsapling.sapling.type.AnnounceEventType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service

public class TransferHistoryService {
    @Autowired
    private TransferHistoryRepository repository;
    @Autowired
    private PeerService peerService;

    @Nullable
    public TransferHistory getTransferHistory(@NotNull User user, @NotNull Torrent torrent) {
        return repository.findByUserAndTorrent(user, torrent).orElse(null);
    }

    @NotNull
    public List<TransferHistory> getTransferHistory(@NotNull Torrent torrent) {
        return repository.findAllByTorrentOrderByUpdatedAt(torrent);
    }

    @NotNull
    public List<TransferHistory> getTransferHistoryActive(@NotNull Torrent torrent) {
        Timestamp timestamp = Timestamp.from(Instant.now().minus(2, ChronoUnit.HOURS));
        return repository.findAllByTorrentAndUpdatedAtAfterOrderByUpdatedAt(torrent, timestamp);
    }

    @NotNull
    public PeerStatus getPeerStatus(@NotNull Torrent torrent) {
        List<TransferHistory> histories = getTransferHistoryActive(torrent);
        int complete = 0;
        int incomplete = 0;
        int downloaders = 0;
        int downloaded = 0;
        for (TransferHistory history : histories) {
            if (history.getLastEvent() == AnnounceEventType.PAUSED) { // 部分做种
                downloaders++;
                continue;
            }
            if (history.isHaveCompleteHistory()) { // 曾经完成过下载
                downloaded++;
            }
            if (history.getLeft() == 0) { // 下载报告已完成
                complete++;
            } else { // 下载还未完成 (不和 haveCompleteHistory 合并，因为可以下载多次）
                incomplete++;
            }
        }
        return new PeerStatus(complete, incomplete, downloaded, downloaders);
    }

    @NotNull
    public TransferHistory save(@NotNull TransferHistory transferHistory) {
        return repository.save(transferHistory);
    }

    public record PeerStatus(int complete, int incomplete, int downloaded, int downloaders) {

    }
}
