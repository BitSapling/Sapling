package com.github.bitsapling.sapling.module.group.dto;

import com.github.bitsapling.sapling.module.group.Group;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

@EqualsAndHashCode(callSuper = true)
@Validated
public class GroupDTO extends Group {
    public GroupDTO(@PositiveOrZero Long id, @NotEmpty String name, @NotEmpty String iconUrl, String cssClassName, @Positive Long promotion) {
        super(id, name, iconUrl, cssClassName, promotion);
    }
}
