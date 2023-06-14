package com.github.bitsapling.sapling.module.team;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team> implements TeamService {
    @Nullable
    public Team getTeam(@NotNull String name) {
        LambdaQueryWrapper<Team> wrapper = Wrappers
                .lambdaQuery(Team.class)
                .eq(Team::getName, name);
        return baseMapper.selectOne(wrapper);
    }
}
