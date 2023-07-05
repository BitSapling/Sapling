package com.github.bitsapling.sapling.module.permission.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Valid
@Data
@EqualsAndHashCode
public class DraftedPermission {
    @Positive
    private Long group;
    @NotEmpty
    private String permission;
}
