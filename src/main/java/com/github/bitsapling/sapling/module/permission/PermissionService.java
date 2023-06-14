package com.github.bitsapling.sapling.module.permission;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {
    @Autowired
    private PermissionMapper mapper;

    @NotNull
    public List<Permission> getAllPermissions() {
        return mapper.selectList(Wrappers.lambdaQuery(Permission.class));
    }

    @Nullable
    public Permission getPermission(@NotNull Long id) {
        return mapper.selectById(id);
    }

    @Nullable
    public Permission getPermission(@NotNull String name) {
        LambdaQueryWrapper<Permission> wrapper = Wrappers
                .lambdaQuery(Permission.class)
                .eq(Permission::getPermission, name);
        return mapper.selectOne(wrapper);
    }

    @Nullable
    public List<Permission> getPermissionByGroup(@NotNull Long groupId) {
        LambdaQueryWrapper<Permission> wrapper = Wrappers
                .lambdaQuery(Permission.class)
                .eq(Permission::getGroup, groupId);
        return mapper.selectList(wrapper);
    }

    public int addPermission(@NotNull Permission perm) {
        return mapper.insert(perm);
    }

    public int deletePermission(@NotNull Permission perm) {
        return mapper.deleteById(perm.getId());
    }

    public int deletePermission(@NotNull Long id) {
        return mapper.deleteById(id);
    }

    public int updatePermission(@NotNull Permission perm) {
        return mapper.updateById(perm);
    }
}
