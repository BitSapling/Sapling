package com.github.bitsapling.sapling.controller.dto.response;

import com.github.bitsapling.sapling.entity.User;
import com.github.bitsapling.sapling.objects.ResponsePojo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.springframework.validation.annotation.Validated;

@EqualsAndHashCode(callSuper = true)
@Data
@Validated
public class UserTinyResponseDTO extends ResponsePojo {
    private long id;
    private String username;
    private UserGroupResponseDTO group;
    private String avatar;

    public UserTinyResponseDTO(@NotNull User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.group = new UserGroupResponseDTO(user.getGroup());
        this.avatar = user.getAvatar();
    }
}
