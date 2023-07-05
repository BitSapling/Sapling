package com.github.bitsapling.sapling.module.group;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.github.bitsapling.sapling.module.common.CommonService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class GroupService extends ServiceImpl<GroupMapper, Group> implements CommonService<Group> {
    @Nullable
    public Group getGroup(@NotNull String name) {
        return ChainWrappers.lambdaQueryChain(Group.class)
                .eq(Group::getName, name)
                .one();
    }
}
