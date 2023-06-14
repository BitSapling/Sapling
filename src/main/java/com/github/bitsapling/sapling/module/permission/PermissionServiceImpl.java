package com.github.bitsapling.sapling.module.permission;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
    @Nullable
    public Permission getPermission(@NotNull String name) {
        LambdaQueryWrapper<Permission> wrapper = Wrappers
                .lambdaQuery(Permission.class)
                .eq(Permission::getPermission, name);
        return baseMapper.selectOne(wrapper);
    }

    @Nullable
    public List<Permission> getPermissionByGroup(@NotNull Long groupId) {
        LambdaQueryWrapper<Permission> wrapper = Wrappers
                .lambdaQuery(Permission.class)
                .eq(Permission::getGroup, groupId);
        return baseMapper.selectList(wrapper);
    }
}
