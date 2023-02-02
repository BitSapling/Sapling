package com.ghostchu.sapling.domain.model;

import com.ghostchu.sapling.domain.type.TorrentStatus;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Entity
@Table(name = "torrents")
public class Torrent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long torrentId;
    @Column(name = "info_hash")
    @NotNull
    private String infoHash;
    @Column(name = "user_id")
    private long userId;
    @Column(name = "title")
    @NotNull
    private String title;
    @Column(name = "sub_title")
    @NotNull
    private String subTitle;
    @Column(name = "nfo_file")
    @Nullable
    private String nfoFile;
    @Column(name = "description")
    @NotNull
    private String description;
    @Column(name = "category")
    private int category;
    @Column(name = "creator_group")
    private int creatorGroup;
    @Column(name = "media")
    private int media;
    @Column(name = "encoding")
    private int encoding;
    @Column(name = "resolution")
    private int resolution;
    @Column(name = "anonymous")
    private boolean anonymous;
    @Column(name = "torrent_status")
    @NotNull
    private TorrentStatus torrentStatus;
    //    @Column(name = "fields")
//    @NotNull
//    private Map<String, Object> fields;
    @Column(name = "tags")
    @NotNull
    @OneToMany
    private List<Tag> tags;
    @Column(name = "promotion_id")
    private long promotionId;

    public Torrent(long torrentId, long userId, @NotNull String infoHash, @NotNull String title, @NotNull String subTitle, @Nullable String nfoFile, @NotNull String description, int category, int creatorGroup, int media, int encoding, int resolution, boolean anonymous, @NotNull TorrentStatus torrentStatus, @NotNull List<Tag> tags) {
        this.torrentId = torrentId;
        this.userId = userId;
        this.infoHash = infoHash;
        this.title = title;
        this.subTitle = subTitle;
        this.nfoFile = nfoFile;
        this.description = description;
        this.category = category;
        this.creatorGroup = creatorGroup;
        this.media = media;
        this.encoding = encoding;
        this.resolution = resolution;
        this.torrentStatus = torrentStatus;
        this.anonymous = anonymous;
        this.tags = tags;
        //  this.fields = fields;
    }

    public Torrent() {

    }

    public @NotNull String getInfoHash() {
        return infoHash;
    }

    public long getTorrentId() {
        return torrentId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public @NotNull String getTitle() {
        return title;
    }

    public void setTitle(@NotNull String title) {
        this.title = title;
    }

    public @NotNull String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(@NotNull String subTitle) {
        this.subTitle = subTitle;
    }

    public @Nullable String getNfoFile() {
        return nfoFile;
    }

    public void setNfoFile(@Nullable String nfoFile) {
        this.nfoFile = nfoFile;
    }

    public @NotNull String getDescription() {
        return description;
    }

    public void setDescription(@NotNull String description) {
        this.description = description;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getCreatorGroup() {
        return creatorGroup;
    }

    public void setCreatorGroup(int creatorGroup) {
        this.creatorGroup = creatorGroup;
    }

    public int getMedia() {
        return media;
    }

    public void setMedia(int media) {
        this.media = media;
    }

    public int getEncoding() {
        return encoding;
    }

    public void setEncoding(int encoding) {
        this.encoding = encoding;
    }

    public int getResolution() {
        return resolution;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public @NotNull List<Tag> getTags() {
        return tags;
    }

    public void setTags(@NotNull List<Tag> tags) {
        this.tags = tags;
    }

//    public @NotNull Map<Integer, Object> getFields() {
//        return fields;
//    }
//
//    public void setFields(@NotNull Map<Integer, Object> fields) {
//        this.fields = fields;
//    }

    public @NotNull TorrentStatus getTorrentStatus() {
        return torrentStatus;
    }

    public void setTorrentStatus(@NotNull TorrentStatus torrentStatus) {
        this.torrentStatus = torrentStatus;
    }

    public long getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(long promotionId) {
        this.promotionId = promotionId;
    }
}
