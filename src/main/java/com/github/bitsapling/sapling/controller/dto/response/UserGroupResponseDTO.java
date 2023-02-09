package com.github.bitsapling.sapling.controller.dto.response;

import com.github.bitsapling.sapling.entity.UserGroup;
import com.github.bitsapling.sapling.objects.ResponsePojo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserGroupResponseDTO extends ResponsePojo {
    private long id;
    private String slug;
    private String displayName;
    public UserGroupResponseDTO(@NotNull UserGroup userGroup){
        this.id = userGroup.getId();
        this.slug = userGroup.getSlug();
        this.displayName = userGroup.getDisplayName();
    }
}
