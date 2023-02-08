package com.github.bitsapling.sapling.controller.bean;

import com.github.bitsapling.sapling.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@Data
public class CategoryBean {
    private final long id;
    private final String slug;
    private final String name;
    private final String icon;

    public CategoryBean(@NotNull Category category) {
        this.id = category.getId();
        this.slug = category.getSlug();
        this.name = category.getName();
        this.icon = category.getIcon();
    }
}
