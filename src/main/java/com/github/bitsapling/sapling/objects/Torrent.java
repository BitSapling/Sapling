package com.github.bitsapling.sapling.objects;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
@AllArgsConstructor
@Data
public class Torrent {
    private final long id;
    private final String infoHash;
    private User user;
    private String title;
    private String subTitle;
    private final long size;
    private long finishes;
    private final Instant createdAt;
    private Instant updatedAt;
    private boolean underReview;
    private boolean anonymous;
    private int type;
    private PromotionPolicy promotionPolicy;
    private int descriptionType;
    private String description;
}
