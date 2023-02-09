package com.github.bitsapling.sapling.controller.dto.response;

import com.github.bitsapling.sapling.entity.Category;
import com.github.bitsapling.sapling.objects.ResponsePojo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryResponseDTO extends ResponsePojo {
    private long id;
    private String slug;
    private String name;
    private String icon;

    public CategoryResponseDTO(@NotNull Category category) {
        this.id = category.getId();
        this.slug = category.getSlug();
        this.name = category.getName();
        this.icon = category.getIcon();
    }
}
