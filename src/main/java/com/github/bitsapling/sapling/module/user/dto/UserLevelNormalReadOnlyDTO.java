package com.github.bitsapling.sapling.module.user.dto;

import com.github.bitsapling.sapling.module.group.Group;
import com.github.bitsapling.sapling.module.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode
@Data
public class UserLevelNormalReadOnlyDTO {
    private Long id;
    private String username;
    private String nickname;
    private Group group;
    private String avatarUrl;
    private LocalDateTime joinedAt;
    private LocalDateTime lastSeenAt;
    private String bio;
    private Boolean isBanned;

    public UserLevelNormalReadOnlyDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.group = user.getGroup();
        this.avatarUrl = user.getAvatarUrl();
        this.joinedAt = user.getJoinedAt();
        this.lastSeenAt = user.getLastSeenAt();
        this.bio = user.getBio();
        this.isBanned = user.getIsBanned();
    }

}
