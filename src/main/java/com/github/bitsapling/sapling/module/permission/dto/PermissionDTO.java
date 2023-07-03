package com.github.bitsapling.sapling.module.permission.dto;

import com.github.bitsapling.sapling.module.permission.Permission;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

@EqualsAndHashCode(callSuper = true)
@Validated
public class PermissionDTO extends Permission {
    public PermissionDTO(@PositiveOrZero Long id, @Positive Long group, @NotEmpty String permission, @NotNull Boolean value) {
        super(id, group, permission, value);
    }
}
