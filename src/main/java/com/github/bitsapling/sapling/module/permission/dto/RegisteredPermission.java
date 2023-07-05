package com.github.bitsapling.sapling.module.permission.dto;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Valid
@Data
@EqualsAndHashCode
public class RegisteredPermission {
    private Long id;
    private Long group;
    private String permission;
}
