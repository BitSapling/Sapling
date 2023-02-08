package com.github.bitsapling.sapling.controller.bean;

import com.github.bitsapling.sapling.entity.User;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

@Data
public class UserBean {
    private final long id;
    private final String username;
    private final String avatar;
    private final String customTitle;
    private final String signature;
    private final String language;
    private final String uploadBandwidth;
    private final String downloadBandwidth;
    private final long downloaded;
    private final long uploaded;
    private final String isp;
    private final long seedingTime;
    private final long createdAt;
    private final BigDecimal karma;

    public UserBean(@NotNull User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.avatar = user.getAvatar();
        this.customTitle = user.getCustomTitle();
        this.signature = user.getSignature();
        this.language = user.getLanguage();
        this.uploadBandwidth = user.getUploadBandwidth();
        this.downloadBandwidth = user.getDownloadBandwidth();
        this.downloaded = user.getDownloaded();
        this.uploaded = user.getUploaded();
        this.isp = user.getIsp();
        this.seedingTime = user.getSeedingTime();
        this.createdAt = user.getCreatedAt().getTime();
        this.karma = user.getKarma();
    }
}
