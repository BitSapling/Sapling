package com.ghostchu.sapling.domain.type;

import org.jetbrains.annotations.NotNull;

public class Promotion {
    private final long promotionId;
    @NotNull
    private final String name;
    @NotNull
    private String displayName;
    private double uploadOff;
    private double downloadOff;

    public Promotion(long promotionId, @NotNull String name, String displayName, double uploadOff, double downloadOff) {
        this.promotionId = promotionId;
        this.name = name;
        this.displayName = displayName;
        this.uploadOff = uploadOff;
        this.downloadOff = downloadOff;
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
