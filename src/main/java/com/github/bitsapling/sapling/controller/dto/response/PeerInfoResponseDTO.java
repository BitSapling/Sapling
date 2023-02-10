package com.github.bitsapling.sapling.controller.dto.response;

import com.github.bitsapling.sapling.entity.Peer;
import com.github.bitsapling.sapling.objects.ResponsePojo;
import com.github.bitsapling.sapling.type.PrivacyLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@Data
public class PeerInfoResponseDTO extends ResponsePojo {
    private long id;
    private UserBasicResponseDTO user;
    private String infoHash;
    private String peerId;
    private String userAgent;
    private long uploaded;
    private long downloaded;
    private long left;
    private boolean seeder;
    private boolean partialSeeder;
    private Timestamp updateAt;
    private long seedingTime;
    private long uploadSpeed;
    private long downloadSpeed;

    public PeerInfoResponseDTO(Peer peer) {
        this.id = peer.getId();
        if (peer.getUser().getPrivacyLevel().ordinal() > PrivacyLevel.MEDIUM.ordinal()) {
            this.user = null;
        } else {
            this.user = new UserBasicResponseDTO(peer.getUser());
        }
        this.infoHash = peer.getInfoHash();
        this.peerId = peer.getPeerId();
        this.userAgent = peer.getUserAgent();
        this.uploaded = peer.getUploaded();
        this.downloaded = peer.getDownloaded();
        this.left = peer.getLeft();
        this.seeder = peer.isSeeder();
        this.partialSeeder = peer.isPartialSeeder();
        this.updateAt = peer.getUpdateAt();
        this.seedingTime = peer.getSeedingTime();
        this.uploadSpeed = peer.getUploadSpeed();
        this.downloadSpeed = peer.getDownloadSpeed();
    }
}
