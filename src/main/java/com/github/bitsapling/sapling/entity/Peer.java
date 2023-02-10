package com.github.bitsapling.sapling.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "peers",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"ip", "port", "info_hash"})
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Peer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private long id;
    @Column(name = "ip", nullable = false, updatable = false)
    private String ip;
    @Column(name = "port", nullable = false, updatable = false)
    private int port;
    @Column(name = "info_hash", nullable = false, updatable = false)
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
    @Column(name = "partial_seeder", nullable = false)
    private boolean partialSeeder;
    @Column(name = "passkey", nullable = false)
    private String passKey;
    @Column(name = "update_at", nullable = false)
    private Timestamp updateAt;
    @Column(name = "seeding_time", nullable = false)
    private long seedingTime;
    @Column(name = "upload_speed", nullable = false)
    private long uploadSpeed;
    @Column(name = "download_speed", nullable = false)
    private long downloadSpeed;
    @PrimaryKeyJoinColumn
    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JsonBackReference
    private User user;

}
