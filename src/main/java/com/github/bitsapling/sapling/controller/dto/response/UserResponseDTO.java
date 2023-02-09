package com.github.bitsapling.sapling.controller.dto.response;

import com.github.bitsapling.sapling.entity.User;
import com.github.bitsapling.sapling.objects.ResponsePojo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserResponseDTO extends ResponsePojo {
    private long id;
    private String email;
    private String username;
    private UserGroupResponseDTO group;
    private long createdAt;
    private String avatar;
    private String customTitle;
    private String signature;
    private String language;
    private String downloadBandwidth;
    private String uploadBandwidth;
    private long downloaded;
    private long uploaded;
    private long realDownloaded;
    private long realUploaded;
    private String isp;
    private BigDecimal karma;
    private int inviteSlot;
    private long seedingTime;

    public UserResponseDTO(@NotNull User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.group = new UserGroupResponseDTO(user.getGroup());
        this.createdAt = user.getCreatedAt().getTime();
        this.avatar = user.getAvatar();
        this.customTitle = user.getCustomTitle();
        this.signature = user.getSignature();
        this.language = user.getLanguage();
        this.downloadBandwidth = user.getDownloadBandwidth();
        this.uploadBandwidth = user.getUploadBandwidth();
        this.downloaded = user.getDownloaded();
        this.uploaded = user.getUploaded();
        this.realDownloaded = user.getRealDownloaded();
        this.realUploaded = user.getRealUploaded();
        this.isp = user.getIsp();
        this.karma = user.getKarma();
        this.inviteSlot = user.getInviteSlot();
        this.seedingTime = user.getSeedingTime();
    }
}
