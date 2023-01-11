package com.github.bitsapling.sapling.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "peers",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"ip", "port", "infoHash"})
        }
)
@Data
public class Peer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", nullable = false)
    private UUID uuid;
    @Column(name = "ip", nullable = false)
    private String ip;
    @Column(name = "port", nullable = false)
    private int port;
    @Column(name = "info_hash", nullable = false)
    private String infoHash;
    @Column(name = "peer_id", nullable = false)
    private String peerId;
    @Column(name = "peer_id_hash", nullable = false)
    private String peerIdHash;
    @Column(name = "user_agent", nullable = false)
    private String userAgent;
    @Column(name = "uploaded", nullable = false)
    private long uploaded;
    @Column(name = "downloaded", nullable = false)
    private long downloaded;
    @Column(name = "seeder", nullable = false)
    private boolean seeder;
    @Column(name = "torrent_id", nullable = false)
    private long torrentId;
    @Column(name = "user_id", nullable = false)
    private long userId;
    @Column(name = "update_at", nullable = false)
    private long updateAt;
    @Column(name = "connectable", nullable = false)
    private int connectable;
}
