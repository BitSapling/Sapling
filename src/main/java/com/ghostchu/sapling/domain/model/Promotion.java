package com.ghostchu.sapling.domain.model;

import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

@Entity
@Table(name = "promotions")
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long promotionId;
    @Column(name = "name")
    @NotNull
    private String name;
    @Column(name = "display_name")
    @NotNull
    private String displayName;
    @Column(name = "upload_off")
    private double uploadOff;
    @Column(name = "download_off")
    private double downloadOff;
    @Column(name = "priority")
    private int priority;

    public Promotion(long promotionId, @NotNull String name, @NotNull String displayName, double uploadOff, double downloadOff) {
        this.promotionId = promotionId;
        this.name = name;
        this.displayName = displayName;
        this.uploadOff = uploadOff;
        this.downloadOff = downloadOff;
    }

    public Promotion() {

    }

    public @NotNull String getName() {
        return name;
    }

    @NotNull
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(@NotNull String displayName) {
        this.displayName = displayName;
    }

    public double getUploadOff() {
        return uploadOff;
    }

    public void setUploadOff(double uploadOff) {
        this.uploadOff = uploadOff;
    }

    public double getDownloadOff() {
        return downloadOff;
    }

    public void setDownloadOff(double downloadOff) {
        this.downloadOff = downloadOff;
    }

    public long getPromotionId() {
        return promotionId;
    }
}
