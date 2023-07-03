package com.github.bitsapling.sapling.module.user.dto;

import com.github.bitsapling.sapling.module.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@EqualsAndHashCode
@Data
@Validated
public class UserLevelSelfReadOnlyDTO {
    private Long id;
    private String passkey;
    private String username;
    private String nickname;
    private Long loginProvider;
    private String email;
    private Boolean email_confirmed;
    private Long group;
    private String avatarUrl;
    private LocalDateTime joinedAt;
    private LocalDateTime lastSeenAt;
    private String registerIp;
    private String siteLang;
    private String bio;
    private Boolean isBanned;
    private Byte[] preferences;

    public UserLevelSelfReadOnlyDTO(User user) {
        this.id = user.getId();
        this.passkey = user.getPasskey();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.loginProvider = user.getLoginProvider();
        this.email = user.getEmail();
        this.email_confirmed = user.getEmail_confirmed();
        this.group = user.getGroup();
        this.avatarUrl = user.getAvatarUrl();
        this.joinedAt = user.getJoinedAt();
        this.lastSeenAt = user.getLastSeenAt();
        this.registerIp = user.getRegisterIp();
        this.siteLang = user.getSiteLang();
        this.bio = user.getBio();
        this.isBanned = user.getIsBanned();
        this.preferences = user.getPreferences();
    }
}
