package com.github.bitsapling.sapling.module.permission;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.github.bitsapling.sapling.module.common.CommonService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService extends ServiceImpl<PermissionMapper, Permission> implements CommonService<Permission> {
    public List<Permission> getPermissions(@NotNull Object identifier) {
        return ChainWrappers.lambdaQueryChain(Permission.class)
                .eq(Permission::getId, identifier)
                .or(w -> w.eq(Permission::getPermission, identifier))
                .list();
    }

    @NotNull
    public List<Permission> getPermissionByGroup(@NotNull Long groupId) {
        return ChainWrappers.lambdaQueryChain(Permission.class)
                .eq(Permission::getGroup, groupId)
                .list();
    }

}
