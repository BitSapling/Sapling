package com.github.bitsapling.sapling.module.tag.dto;

import com.github.bitsapling.sapling.module.tag.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TagModuleDtoMapper {
    TagModuleDtoMapper INSTANCE = Mappers.getMapper(TagModuleDtoMapper.class);

    RegisteredTag toRegisteredObject(Tag tag);
}
