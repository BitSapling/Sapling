package com.github.bitsapling.sapling.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "peers",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"ip", "peer_id", "info_hash"})
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
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
    @Column(name = "user_agent", nullable = false)
    private String userAgent;
    @Column(name = "uploaded", nullable = false)
    private long uploaded;
    @Column(name = "downloaded", nullable = false)
    private long downloaded;
    @Column(name = "to_go", nullable = false)
    private long left;
    @Column(name = "seeder", nullable = false)
    private boolean seeder;
    @PrimaryKeyJoinColumn(name = "torrent")
    @OneToOne(cascade = CascadeType.ALL)
    private Torrent torrent;
    @PrimaryKeyJoinColumn(name="user")
    @OneToOne(cascade = CascadeType.ALL)
    private User user;
    @Column(name = "update_at", nullable = false)
    private Instant updateAt;
}
