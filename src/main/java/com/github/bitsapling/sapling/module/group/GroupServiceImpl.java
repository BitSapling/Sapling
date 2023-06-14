package com.github.bitsapling.sapling.module.group;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements GroupService {
    @Nullable
    public Group getGroup(@NotNull String name) {
        LambdaQueryWrapper<Group> wrapper = Wrappers
                .lambdaQuery(Group.class)
                .eq(Group::getName, name);
        return baseMapper.selectOne(wrapper);
    }
}
