package com.github.bitsapling.sapling.module.user.dto;

import com.github.bitsapling.sapling.module.user.User;
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Validated
public class UserLevelAdminReadOnlyDTO extends User {

    public UserLevelAdminReadOnlyDTO(@Positive Long id, String passkey, String username, String nickname,
                                     String password, Long loginProvider, String loginIdentifier,
                                     String email, Boolean email_confirmed, Long group,
                                     String avatarUrl, LocalDateTime joinedAt, LocalDateTime lastSeenAt,
                                     String registerIp, String siteLang, String bio, Boolean isBanned,
                                     Byte[] preferences) {
        super(id, passkey, username, nickname, password, loginProvider, loginIdentifier, email, email_confirmed, group, avatarUrl, joinedAt, lastSeenAt, registerIp, siteLang, bio, isBanned, preferences);
    }

}
