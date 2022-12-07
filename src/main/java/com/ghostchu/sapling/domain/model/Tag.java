package com.ghostchu.sapling.domain.model;

import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long tagId;
    @Column(name = "name")
    @NotNull
    private String name;
    @Column(name = "color")
    @NotNull
    private String hexColor;

    public Tag(long tagId, @NotNull String name, @NotNull String hexColor) {
        this.tagId = tagId;
        this.name = name;
        this.hexColor = hexColor;
    }

    public Tag() {

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
