package com.github.bitsapling.sapling.controller.dto.response;

import com.github.bitsapling.sapling.entity.PromotionPolicy;
import com.github.bitsapling.sapling.entity.Tag;
import com.github.bitsapling.sapling.entity.Torrent;
import com.github.bitsapling.sapling.objects.ResponsePojo;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Validated
public class TorrentBasicResponseDTO extends ResponsePojo {
    private final long id;
    private final String infoHash;
    private final UserTinyResponseDTO user;
    private final String title;
    private final String subTitle;
    private final long size;
    private final Timestamp createdAt;
    private final boolean underReview;
    private final boolean anonymous;
    private final CategoryResponseDTO category;
    private final PromotionPolicy promotionPolicy;
    private final List<String> tag;

    public TorrentBasicResponseDTO(Torrent torrent){
        super(0);
        this.id = torrent.getId();
        this.infoHash = torrent.getInfoHash();
        if(torrent.isAnonymous()){
            this.user = null;
        }else{
            this.user = new UserTinyResponseDTO(torrent.getUser());
        }
        this.title = torrent.getTitle();
        this.subTitle = torrent.getSubTitle();
        this.size = torrent.getSize();
        this.createdAt = torrent.getCreatedAt();
        this.underReview = torrent.isUnderReview();
        this.anonymous = torrent.isAnonymous();
        this.category = new CategoryResponseDTO(torrent.getCategory());
        this.promotionPolicy = torrent.getPromotionPolicy();
        this.tag = torrent.getTag().stream().map(Tag::getName).toList();
    }
}
