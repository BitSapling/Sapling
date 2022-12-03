package com.ghostchu.sapling.domain.entity;

import com.ghostchu.sapling.util.NumberUtil;
import com.ghostchu.sapling.util.PasswordHash;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class User {
    private long userId;
    private int groupId;
    @NotNull
    private String email;
    private boolean emailConfirmed;
    @NotNull
    private String nickname;
    @NotNull
    private String passwordHash;
    @NotNull
    private UUID passkey;
    private long inviterUid;
    @NotNull
    private Date joinDate;
    @NotNull
    private Date recentActivityDate;
    @NotNull
    private BigDecimal uploadBytes;
    @NotNull
    private BigDecimal actualUploadBytes;
    @NotNull
    private BigDecimal downloadBytes;
    @NotNull
    private BigDecimal actualDownloadBytes;
    @NotNull
    private BigDecimal seedingTime;
    @NotNull
    private BigDecimal downloadingTime;
    @NotNull
    private String avatar;
    @NotNull
    private BigDecimal karma;
    @NotNull
    private BigDecimal seedingScore;
    @NotNull
    private List<String> recentIps;
    private boolean parked;
    @NotNull
    private String introduction;
    @NotNull
    private String regionCode;
    @NotNull
    private String languageCode;
    private boolean showAds;
    @NotNull
    private String signature;
    private int inviteSlots;
    private boolean downloadPrivilege;
    @Nullable
    private String downloadPrivilegeReason;

    private void updateDownloadPrivilege(boolean access, @Nullable String reason) {
        downloadPrivilege = access;
        downloadPrivilegeReason = reason;
    }

    public boolean verifyPassword(@NotNull String password) {
        return PasswordHash.verify(password, this.passwordHash);
    }

    @NotNull
    public String getShareRatioText() {
        if (downloadBytes.compareTo(BigDecimal.ZERO) == 0) {
            return "Inf";
        }
        return NumberUtil.formatShareRatio(uploadBytes.divide(downloadBytes, RoundingMode.HALF_UP));
    }

    @NotNull
    public BigDecimal getShareRatio() {
        if (downloadBytes.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.valueOf(Long.MAX_VALUE);
        }
        return uploadBytes.divide(downloadBytes, RoundingMode.HALF_UP);
    }

    public long getUserId() {
        return userId;
    }

    public int getGroupId() {
        return groupId;
    }

    @NotNull
    public String getEmail() {
        return email;
    }

    @NotNull
    public String getNickname() {
        return nickname;
    }

    @NotNull
    public UUID getPasskey() {
        return passkey;
    }

    public long getInviterUid() {
        return inviterUid;
    }

    @NotNull
    public Date getJoinDate() {
        return joinDate;
    }

    @NotNull
    public Date getRecentActivityDate() {
        return recentActivityDate;
    }

    @NotNull
    public BigDecimal getUploadBytes() {
        return uploadBytes;
    }

    @NotNull
    public BigDecimal getActualUploadBytes() {
        return actualUploadBytes;
    }

    @NotNull
    public BigDecimal getDownloadBytes() {
        return downloadBytes;
    }

    @NotNull
    public BigDecimal getActualDownloadBytes() {
        return actualDownloadBytes;
    }

    @NotNull
    public BigDecimal getSeedingTime() {
        return seedingTime;
    }

    @NotNull
    public BigDecimal getDownloadingTime() {
        return downloadingTime;
    }

    @NotNull
    public String getAvatar() {
        return avatar;
    }

    @NotNull
    public BigDecimal getKarma() {
        return karma;
    }

    @NotNull
    public BigDecimal getSeedingScore() {
        return seedingScore;
    }

    @NotNull
    public List<String> getRecentIps() {
        return recentIps;
    }

    public boolean isParked() {
        return parked;
    }

    @NotNull
    public String getIntroduction() {
        return introduction;
    }

    @NotNull
    public String getRegionCode() {
        return regionCode;
    }

    @NotNull
    public String getLanguageCode() {
        return languageCode;
    }

    public boolean isShowAds() {
        return showAds;
    }

    @NotNull
    public String getSignature() {
        return signature;
    }

    public int getInviteSlots() {
        return inviteSlots;
    }
}
