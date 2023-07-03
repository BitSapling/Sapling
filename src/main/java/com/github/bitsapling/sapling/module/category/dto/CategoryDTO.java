package com.github.bitsapling.sapling.module.category.dto;

import com.github.bitsapling.sapling.module.category.Category;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

@EqualsAndHashCode(callSuper = true)
@Validated
public class CategoryDTO extends Category {

    public CategoryDTO(@PositiveOrZero Long id, @NotEmpty String name, @NotEmpty String iconUrl, String cssClassName, @NotEmpty String permissionName) {
        super(id, name, iconUrl, cssClassName, permissionName);
    }
}
