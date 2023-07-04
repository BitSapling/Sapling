package com.github.bitsapling.sapling.module.tag.dto;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Valid
@EqualsAndHashCode
@Data
public class DraftedTag {
    private String name;
}
