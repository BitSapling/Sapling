package com.github.bitsapling.sapling.entity;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "torrents",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"info_hash"})
        }
)
@Transactional
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
    @PrimaryKeyJoinColumn(name = "user")
    @OneToOne(cascade = CascadeType.REFRESH ,fetch = FetchType.EAGER)
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
    @Column(name = "draft", nullable = false)
    private boolean draft;
    @Column(name = "under_review", nullable = false)
    private boolean underReview;
    @Column(name = "deleted", nullable = false)
    private boolean deleted;
    @Column(name = "anonymous", nullable = false)
    private boolean anonymous;
    @Column(name = "type", nullable = false)
    private int type;
    @OneToOne(cascade = CascadeType.REFRESH ,fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn(name = "promotion_policy")
    private PromotionPolicyEntity promotionPolicy;
    @Column(name = "description_type", nullable = false)
    private int descriptionType;
    @Column(name = "description", nullable = false)
    private String description;


}
