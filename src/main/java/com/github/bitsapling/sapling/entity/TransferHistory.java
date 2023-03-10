package com.github.bitsapling.sapling.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.github.bitsapling.sapling.type.AnnounceEventType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "transfer_history",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "torrent_id"})
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private long id;
    @PrimaryKeyJoinColumn
    @ManyToOne
    @JsonBackReference
    private User user;
    @PrimaryKeyJoinColumn
    @ManyToOne
    @JsonBackReference
    private Torrent torrent;
    @Column(name = "to_go", nullable = false)
    private long left;
    @Column(name = "started_at", nullable = false, updatable = false)
    private Timestamp startedAt;
    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;
    @Column(name = "uploaded", nullable = false)
    private long uploaded;
    @Column(name = "downloaded", nullable = false)
    private long downloaded;
    @Column(name = "actual_uploaded", nullable = false)
    private long actualUploaded;
    @Column(name = "actual_downloaded", nullable = false)
    private long actualDownloaded;
    @Column(name = "upload_speed", nullable = false)
    private long uploadSpeed;
    @Column(name = "download_speed", nullable = false)
    private long downloadSpeed;
    @Column(name = "last_event", nullable = false)
    private AnnounceEventType lastEvent;
    @Column(name = "have_complete_history", nullable = false)
    private boolean haveCompleteHistory;
}
