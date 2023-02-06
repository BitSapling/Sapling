package com.github.bitsapling.sapling.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Torrent  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private  long id;
    private  String infoHash;
    private User user;
    private String title;
    private String subTitle;
    private  long size;
    private long finishes;
    private  Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean underReview;
    private boolean anonymous;
    private int type;
    private PromotionPolicy promotionPolicy;
    private int descriptionType;
    private String description;
}
