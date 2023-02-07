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
@Table(name = "torrents",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"info_hash"})
        }
)

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Torrent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private long id;
    @Column(name = "info_hash", nullable = false, updatable = false)
    private String infoHash;
    @PrimaryKeyJoinColumn
    @Cascade({CascadeType.ALL})
    @ManyToOne
    @JsonBackReference
    private User user;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "sub_title", nullable = false)
    private String subTitle;
    @Column(name = "size", nullable = false, updatable = false)
    private long size;
    @Column(name = "finishes", nullable = false)
    private long finishes;
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;
    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;
    @Column(name = "under_review", nullable = false)
    private boolean underReview;
    @Column(name = "anonymous", nullable = false)
    private boolean anonymous;
    @Column(name = "type", nullable = false)
    private int type;
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    @ManyToOne
    @PrimaryKeyJoinColumn
    @JsonBackReference
    private PromotionPolicy promotionPolicy;
    @Column(name = "description_type", nullable = false)
    private int descriptionType;
    @Column(name = "description", nullable = false)
    private String description;


}