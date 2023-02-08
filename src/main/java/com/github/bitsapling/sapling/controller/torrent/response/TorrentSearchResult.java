package com.github.bitsapling.sapling.controller.torrent.response;

import com.github.bitsapling.sapling.controller.bean.CategoryBean;
import com.github.bitsapling.sapling.controller.bean.UserBean;
import com.github.bitsapling.sapling.entity.Torrent;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
@Data
public class TorrentSearchResult {
    private final long id;
    private final String infoHash;
    private final UserBean user;
    private final String title;
    private final String subTitle;
    private final long size;
    private final long finishes;
    private final CategoryBean category;
    private final String promotionPolicy;

    public TorrentSearchResult(@NotNull Torrent torrent, boolean seeAnonymous){
        this.id = torrent.getId();
        this.infoHash = torrent.getInfoHash();
        if(torrent.isAnonymous() && !seeAnonymous){
            this.user = null;
        }else{
            this.user = new UserBean(torrent.getUser());
        }
        this.title = torrent.getTitle();
        this.subTitle = torrent.getSubTitle();
        this.size = torrent.getSize();
        this.finishes = torrent.getFinishes();
        this.category = new CategoryBean(torrent.getCategory());
        this.promotionPolicy = torrent.getPromotionPolicy().getDisplayName();
    }
}
