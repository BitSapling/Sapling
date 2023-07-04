package com.github.bitsapling.sapling.module.permission.dto;

import com.github.bitsapling.sapling.module.permission.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PermissionModuleDtoMapper {
    PermissionModuleDtoMapper INSTANCE = Mappers.getMapper(PermissionModuleDtoMapper.class);

    RegisteredPermission toRegisteredObject(Permission permission);
}
