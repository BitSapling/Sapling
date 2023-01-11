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
    @Column(name = "uuid")
    private UUID uuid;
    @Column(name = "ip")
    private String ip;
    @Column(name = "port")
    private int port;
    @Column(name = "info_hash")
    private String infoHash;
    @Column(name = "peer_id")
    private String peerId;
    @Column(name = "peer_id_hash")
    private String peerIdHash;
    @Column(name = "user_agent")
    private String userAgent;
    @Column(name = "uploaded")
    private long uploaded;
    @Column(name = "downloaded")
    private long downloaded;
    @Column(name = "seeder")
    private boolean seeder;
    @Column(name = "torrent_id")
    private long torrentId;
    @Column(name = "user_id")
    private long userId;
    @Column(name = "update_at")
    private long updateAt;
    @Column(name = "connectable")
    private boolean connectable;
}
