package com.github.bitsapling.sapling.controller.bean;

import com.github.bitsapling.sapling.entity.UserGroup;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class UserGroupBean {
    private long id;
    private String code;
    private String displayName;
    public UserGroupBean(@NotNull UserGroup userGroup){
        this.id = userGroup.getId();
        this.code = userGroup.getCode();
        this.displayName = userGroup.getDisplayName();
    }
}
