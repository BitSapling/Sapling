package com.github.bitsapling.sapling.controller.dto.response;

import com.github.bitsapling.sapling.entity.PromotionPolicy;
import com.github.bitsapling.sapling.entity.Tag;
import com.github.bitsapling.sapling.entity.Torrent;
import com.github.bitsapling.sapling.objects.ResponsePojo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class TorrentInfoResponseDTO extends ResponsePojo {
    private long id;
    private String infoHash;
    private UserResponseDTO user;
    private String title;
    private String subTitle;
    private long size;
    private long finishes;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean underReview;
    private CategoryResponseDTO category;
    private PromotionPolicy promotionPolicy;
    private String description;
    private List<String> tag;

    public TorrentInfoResponseDTO(Torrent torrent){
        super(0);
        this.id = torrent.getId();
        this.infoHash = torrent.getInfoHash();
        if(torrent.isAnonymous()){
            this.user = null;
        }else{
            this.user = new UserResponseDTO(torrent.getUser());
        }
        this.title = torrent.getTitle();
        this.subTitle = torrent.getSubTitle();
        this.size = torrent.getSize();
        this.finishes = torrent.getFinishes();
        this.createdAt = torrent.getCreatedAt();
        this.updatedAt = torrent.getUpdatedAt();
        this.underReview = torrent.isUnderReview();
        this.category = new CategoryResponseDTO(torrent.getCategory());
        this.promotionPolicy = torrent.getPromotionPolicy();
        this.description = torrent.getDescription();
        this.tag = torrent.getTag().stream().map(Tag::getName).toList();
    }
}
