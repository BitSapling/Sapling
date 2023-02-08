package com.github.bitsapling.sapling.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.sql.Timestamp;

@Entity
@Table(name = "transfer_history",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"id"}),
                @UniqueConstraint(columnNames = {"user_id"}),
                @UniqueConstraint(columnNames = {"torrent_id"})
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
    @Cascade({CascadeType.ALL})
    private User user;
    @PrimaryKeyJoinColumn
    @ManyToOne
    @JsonBackReference
    @Cascade({CascadeType.ALL})
    private Torrent torrent;
    @Column(name = "to_go", nullable = false)
    private long left;
    @Column(name = "started_at", nullable = false, updatable = false)
    private Timestamp startedAt;
    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;
}
