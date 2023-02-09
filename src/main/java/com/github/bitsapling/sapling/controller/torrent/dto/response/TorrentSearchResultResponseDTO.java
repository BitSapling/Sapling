package com.github.bitsapling.sapling.controller.torrent.dto.response;

import com.github.bitsapling.sapling.controller.dto.response.CategoryResponseDTO;
import com.github.bitsapling.sapling.controller.dto.response.UserResponseDTO;
import com.github.bitsapling.sapling.entity.Torrent;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
@Data
public class TorrentSearchResultResponseDTO {
    private final long id;
    private final String infoHash;
    private final UserResponseDTO user;
    private final String title;
    private final String subTitle;
    private final long size;
    private final long finishes;
    private final CategoryResponseDTO category;
    private final String promotionPolicy;

    public TorrentSearchResultResponseDTO(@NotNull Torrent torrent, boolean seeAnonymous){
        this.id = torrent.getId();
        this.infoHash = torrent.getInfoHash();
        if(torrent.isAnonymous() && !seeAnonymous){
            this.user = null;
        }else{
            this.user = new UserResponseDTO(torrent.getUser());
        }
        this.title = torrent.getTitle();
        this.subTitle = torrent.getSubTitle();
        this.size = torrent.getSize();
        this.finishes = torrent.getFinishes();
        this.category = new CategoryResponseDTO(torrent.getCategory());
        this.promotionPolicy = torrent.getPromotionPolicy().getDisplayName();
    }
}
