package com.github.bitsapling.sapling.controller.torrent.response;

import com.github.bitsapling.sapling.controller.bean.CategoryBean;
import com.github.bitsapling.sapling.controller.bean.UserBean;
import com.github.bitsapling.sapling.entity.PromotionPolicy;
import com.github.bitsapling.sapling.entity.Torrent;
import com.github.bitsapling.sapling.objects.ResponsePojo;
import lombok.Getter;

import java.sql.Timestamp;
@Getter
public class TorrentInfo extends ResponsePojo {
    private final long id;
    private final String infoHash;
    private final UserBean user;
    private final String title;
    private final String subTitle;
    private final long size;
    private final long finishes;
    private final Timestamp createdAt;
    private final Timestamp updatedAt;
    private final boolean underReview;
    private final boolean anonymous;
    private final CategoryBean category;
    private final PromotionPolicy promotionPolicy;
    private final String description;

    public TorrentInfo(Torrent torrent){
        super(0);
        this.id = torrent.getId();
        this.infoHash = torrent.getInfoHash();
        if(torrent.isAnonymous()){
            this.user = null;
        }else{
            this.user = new UserBean(torrent.getUser());
        }
        this.title = torrent.getTitle();
        this.subTitle = torrent.getSubTitle();
        this.size = torrent.getSize();
        this.finishes = torrent.getFinishes();
        this.createdAt = torrent.getCreatedAt();
        this.updatedAt = torrent.getUpdatedAt();
        this.underReview = torrent.isUnderReview();
        this.anonymous = torrent.isAnonymous();
        this.category = new CategoryBean(torrent.getCategory());
        this.promotionPolicy = torrent.getPromotionPolicy();
        this.description = torrent.getDescription();
    }
}