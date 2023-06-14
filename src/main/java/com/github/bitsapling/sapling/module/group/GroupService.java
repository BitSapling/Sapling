package com.github.bitsapling.sapling.module.group;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {
    @Autowired
    private GroupMapper mapper;

    @NotNull
    public List<Group> getGroups() {
        return mapper.selectList(Wrappers.lambdaQuery(Group.class));
    }

    @Nullable
    public Group getGroup(@NotNull Long id) {
        return mapper.selectById(id);
    }

    @Nullable
    public Group getGroup(@NotNull String name) {
        LambdaQueryWrapper<Group> wrapper = Wrappers
                .lambdaQuery(Group.class)
                .eq(Group::getName, name);
        return mapper.selectOne(wrapper);
    }

    public int addGroup(@NotNull Group group) {
        return mapper.insert(group);
    }

    public int deleteGroup(@NotNull Group group) {
        return mapper.deleteById(group.getId());
    }

    public int deleteGroup(@NotNull Long id) {
        return mapper.deleteById(id);
    }

    public int updateGroup(@NotNull Group group) {
        return mapper.updateById(group);
    }
}
