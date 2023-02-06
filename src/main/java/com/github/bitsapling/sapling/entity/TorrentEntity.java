package com.github.bitsapling.sapling.entity;

import jakarta.persistence.*;
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
public class TorrentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    @Column(name = "info_hash", nullable = false)
    private String infoHash;
    @PrimaryKeyJoinColumn
    @Cascade({CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.PERSIST})
    @ManyToOne(fetch = FetchType.EAGER)
    private UserEntity user;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "sub_title", nullable = false)
    private String subTitle;
    @Column(name = "size", nullable = false)
    private long size;
    @Column(name = "finishes", nullable = false)
    private long finishes;
    @Column(name = "created_at", nullable = false)
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
    @ManyToOne(fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
    private PromotionPolicyEntity promotionPolicy;
    @Column(name = "description_type", nullable = false)
    private int descriptionType;
    @Column(name = "description", nullable = false)
    private String description;


}
