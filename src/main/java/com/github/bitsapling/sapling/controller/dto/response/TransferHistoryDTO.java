package com.github.bitsapling.sapling.controller.dto.response;

import com.github.bitsapling.sapling.entity.TransferHistory;
import com.github.bitsapling.sapling.type.PrivacyLevel;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class TransferHistoryDTO {
    private long id;
    private UserBasicResponseDTO user;
    private TorrentBasicResponseDTO torrent;
    private long left;
    private Timestamp startedAt;
    private Timestamp updatedAt;
    private long uploaded;
    private long downloaded;
    private long actualUploaded;
    private long actualDownloaded;
    private long uploadSpeed;
    private long downloadSpeed;

    public TransferHistoryDTO(TransferHistory transferHistory) {
        this.id = transferHistory.getId();
        if (transferHistory.getUser().getPrivacyLevel().ordinal() > PrivacyLevel.MEDIUM.ordinal()) {
            this.user = null;
        } else {
            this.user = new UserBasicResponseDTO(transferHistory.getUser());
        }
        this.torrent = new TorrentBasicResponseDTO(transferHistory.getTorrent());
        this.left = transferHistory.getLeft();
        this.startedAt = transferHistory.getStartedAt();
        this.updatedAt = transferHistory.getUpdatedAt();
        this.uploaded = transferHistory.getUploaded();
        this.downloaded = transferHistory.getDownloaded();
        this.actualUploaded = transferHistory.getActualUploaded();
        this.actualDownloaded = transferHistory.getActualDownloaded();
        this.uploadSpeed = transferHistory.getUploadSpeed();
        this.downloadSpeed = transferHistory.getDownloadSpeed();

    }
}
