package com.github.bitsapling.sapling.module.tag.dto;

import com.github.bitsapling.sapling.module.tag.Tag;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

@EqualsAndHashCode(callSuper = true)
@Validated
public class TagDTO extends Tag {
    public TagDTO(@PositiveOrZero Long id, @NotEmpty String name) {
        super(id, name);
    }
}
