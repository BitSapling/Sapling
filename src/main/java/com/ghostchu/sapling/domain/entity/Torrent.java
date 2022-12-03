package com.ghostchu.sapling.domain.entity;

import com.ghostchu.sapling.domain.type.TorrentStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class Torrent {
    private final long torrentId;
    @NotNull
    private final String infoHash;
    private long userId;
    @NotNull
    private String title;
    @NotNull
    private String subTitle;
    @Nullable
    private String nfoFile;
    @NotNull
    private String description;
    private int category;
    private int creatorGroup;
    private int media;
    private int encoding;
    private int resolution;
    private boolean anonymous;
    @NotNull
    private TorrentStatus torrentStatus;
    @NotNull
    private Map<Integer, Object> fields;
    @NotNull
    private List<Integer> tags;

    public Torrent(long torrentId, long userId, @NotNull String infoHash, @NotNull String title, @NotNull String subTitle, @Nullable String nfoFile, @NotNull String description, int category, int creatorGroup, int media, int encoding, int resolution, boolean anonymous, @NotNull TorrentStatus torrentStatus, @NotNull List<Integer> tags, @NotNull Map<Integer, Object> fields) {
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
        this.fields = fields;
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

    public @NotNull List<Integer> getTags() {
        return tags;
    }

    public void setTags(@NotNull List<Integer> tags) {
        this.tags = tags;
    }

    public @NotNull Map<Integer, Object> getFields() {
        return fields;
    }

    public void setFields(@NotNull Map<Integer, Object> fields) {
        this.fields = fields;
    }

    public @NotNull TorrentStatus getTorrentStatus() {
        return torrentStatus;
    }

    public void setTorrentStatus(@NotNull TorrentStatus torrentStatus) {
        this.torrentStatus = torrentStatus;
    }
}
