package com.github.bitsapling.sapling.module.permission;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.bitsapling.sapling.module.common.CommonService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService extends ServiceImpl<PermissionMapper, Permission> implements CommonService<Permission> {
    @Nullable
    public Permission getPermission(@NotNull Object identifier) {
        return baseMapper.selectOne(lambdaQuery()
                .eq(Permission::getId, identifier)
                .or(w -> w.eq(Permission::getPermission, identifier)));
    }

    @NotNull
    public List<Permission> getPermissionByGroup(@NotNull Long groupId) {
        return baseMapper.selectList(lambdaQuery().eq(Permission::getGroup, groupId));
    }
}
