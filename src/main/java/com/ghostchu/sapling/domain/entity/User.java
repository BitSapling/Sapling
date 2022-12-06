package com.ghostchu.sapling.domain.entity;

import com.ghostchu.sapling.util.NumberUtil;
import com.ghostchu.sapling.util.PasswordHash;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long userId;
    @Column(name = "group_id")
    private int groupId;
    @Column(name = "email")
    @NotNull
    private String email;
    @Column(name = "email_confirmed")
    private boolean emailConfirmed;
    @Column(name = "nickname")
    @NotNull
    private String nickname;
    @Column(name = "password")
    @NotNull
    private String passwordHash;
    @Column(name = "passkey")
    @NotNull
    private UUID passkey;
    @Column(name = "inviter")
    private long inviterUid;
    @Column(name = "join_at")
    @NotNull
    private Date joinDate;
    @Column(name = "recent_activity")
    @NotNull
    private Date recentActivityDate;
    @Column(name = "uploaded")
    @NotNull
    private BigDecimal uploadBytes;
    @Column(name = "actual_uploaded")
    @NotNull
    private BigDecimal actualUploadBytes;
    @Column(name = "downloaded")
    @NotNull
    private BigDecimal downloadBytes;
    @Column(name = "actual_downloaded")
    @NotNull
    private BigDecimal actualDownloadBytes;
    @Column(name = "seeding_time")
    @NotNull
    private BigDecimal seedingTime;
    @Column(name = "downloading_time")
    @NotNull
    private BigDecimal downloadingTime;
    @Column(name = "avatar")
    @NotNull
    private String avatar;
    @Column(name = "karma")
    @NotNull
    private BigDecimal karma;
    @Column(name = "seeding_score")
    @NotNull
    private BigDecimal seedingScore;
    @Column(name = "recent_ips")
    @NotNull
    private List<String> recentIps;
    @Column(name = "parked")
    private boolean parked;
    @Column(name = "introduction")
    @NotNull
    private String introduction;
    @Column(name = "region")
    @NotNull
    private String regionCode;
    @Column(name = "language")
    @NotNull
    private String languageCode;
    @Column(name = "show_ads")
    private boolean showAds;
    @Column(name = "signature")
    @NotNull
    private String signature;
    @Column(name = "invite_slots")
    private int inviteSlots;
    @Column(name = "download_privilege")
    private boolean downloadPrivilege;
    @Column(name = "download_privilege_reason")
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
