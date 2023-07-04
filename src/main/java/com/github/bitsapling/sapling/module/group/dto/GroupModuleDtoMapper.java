package com.github.bitsapling.sapling.module.group.dto;

import com.github.bitsapling.sapling.module.group.Group;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GroupModuleDtoMapper {
    GroupModuleDtoMapper INSTANCE = Mappers.getMapper(GroupModuleDtoMapper.class);

    PublishedGroup toDeployedObject(Group group);
}
