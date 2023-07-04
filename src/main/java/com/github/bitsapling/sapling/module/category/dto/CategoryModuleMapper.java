package com.github.bitsapling.sapling.module.category.dto;

import com.github.bitsapling.sapling.module.category.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryModuleMapper {
    CategoryModuleMapper INSTANCE = Mappers.getMapper(CategoryModuleMapper.class);

    PublishedCategory toPublishedObject(Category announcement);
}
