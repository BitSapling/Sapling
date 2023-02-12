package com.github.bitsapling.sapling.controller.dto.response;

import com.github.bitsapling.sapling.entity.User;
import com.github.bitsapling.sapling.objects.ResponsePojo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@Validated
public class UserBasicResponseDTO extends ResponsePojo {
    private long id;
    private String username;
    private UserGroupResponseDTO group;
    private long createdAt;
    private String avatar;
    private String customTitle;
    private String signature;
    private String downloadBandwidth;
    private String uploadBandwidth;
    private long downloaded;
    private long uploaded;
    private String isp;
    private BigDecimal karma;
    private long seedingTime;

    public UserBasicResponseDTO(@NotNull User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.group = new UserGroupResponseDTO(user.getGroup());
        this.createdAt = user.getCreatedAt().getTime();
        this.avatar = user.getAvatar();
        this.customTitle = user.getCustomTitle();
        this.signature = user.getSignature();
        this.downloadBandwidth = user.getDownloadBandwidth();
        this.uploadBandwidth = user.getUploadBandwidth();
        this.downloaded = user.getDownloaded();
        this.uploaded = user.getUploaded();
        this.isp = user.getIsp();
        this.karma = user.getKarma();
        this.seedingTime = user.getSeedingTime();
    }
}
