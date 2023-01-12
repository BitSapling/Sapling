package com.github.bitsapling.sapling.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
@Entity
@Table(name = "torrents",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"info_hash"})
        }
)
@Data
public class Torrent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private long id;
    @Column(name = "info_hash",nullable = false)
    private String infoHash;
    @Column(name = "user_id",nullable = false)
    private long userId;
    @Column(name = "title",nullable = false)
    private String title;
    @Column(name = "sub_title",nullable = false)
    private String subTitle;
    @Column(name = "size",nullable = false)
    private long size;
    @Column(name = "finishes",nullable = false)
    private long finishes;
    @Column(name = "created_at",nullable = false)
    private Timestamp createdAt;
    @Column(name = "updated_at",nullable = false)
    private Timestamp updatedAt;
    @Column(name = "draft",nullable = false)
    private boolean draft;
    @Column(name = "under_review",nullable = false)
    private boolean underReview;
    @Column(name = "deleted",nullable = false)
    private boolean deleted;
    @Column(name = "anonymous",nullable = false)
    private boolean anonymous;
    @Column(name = "type",nullable = false)
    private int type;
    @Column(name = "description_type",nullable = false)
    private int descriptionType;
    @Column(name = "description",nullable = false)
    private String description;
}
