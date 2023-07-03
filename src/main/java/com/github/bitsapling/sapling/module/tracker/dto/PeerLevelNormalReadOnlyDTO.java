package com.github.bitsapling.sapling.module.tracker.dto;

import com.github.bitsapling.sapling.module.tracker.Peer;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
public class PeerLevelNormalReadOnlyDTO {
    private Long id;
    private Long torrent;
    private Long user;
    private BigInteger uploaded;
    private BigInteger downloaded;
    private BigInteger toGo;
    private LocalDateTime startedAt;
    private LocalDateTime lastAction;
    private String userAgent;
    private BigInteger downloadedOffset;
    private BigInteger uploadedOffset;
    private Boolean connectable;

    public PeerLevelNormalReadOnlyDTO(Peer peer) {
        this.id = peer.getId();
        this.torrent = peer.getTorrent();
        this.user = peer.getUser();
        this.uploaded = peer.getUploaded();
        this.downloaded = peer.getDownloaded();
        this.toGo = peer.getToGo();
        this.startedAt = peer.getStartedAt();
        this.lastAction = peer.getLastAction();
        this.userAgent = peer.getUserAgent();
        this.downloadedOffset = peer.getDownloadedOffset();
        this.uploadedOffset = peer.getUploadedOffset();
        this.connectable = peer.getConnectable();
    }
}
