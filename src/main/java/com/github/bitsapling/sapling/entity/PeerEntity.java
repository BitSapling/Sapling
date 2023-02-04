package com.github.bitsapling.sapling.entity;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.Instant;

@Entity
@Table(name = "peers",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"ip", "port", "info_hash"})
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Transactional
public class PeerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
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
    @Column(name = "passkey", nullable = false)
    private String passKey;
    @Column(name = "update_at", nullable = false)
    private Instant updateAt;

    @Column(name = "seeding_time", nullable = false)
    private Duration seedingTime;
}
