package com.ghostchu.sapling.domain.entity;

import org.jetbrains.annotations.NotNull;

public class Tag {
    private final long tagId;
    @NotNull
    private String name;
    @NotNull
    private String hexColor;

    public Tag(long tagId, @NotNull String name, @NotNull String hexColor) {
        this.tagId = tagId;
        this.name = name;
        this.hexColor = hexColor;
    }

    public long getTagId() {
        return tagId;
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @NotNull
    public String getHexColor() {
        return hexColor;
    }

    public void setHexColor(@NotNull String hexColor) {
        this.hexColor = hexColor;
    }
}
