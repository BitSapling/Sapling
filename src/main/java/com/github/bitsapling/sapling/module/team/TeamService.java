package com.github.bitsapling.sapling.module.team;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.github.bitsapling.sapling.module.common.CommonService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class TeamService extends ServiceImpl<TeamMapper, Team> implements CommonService<Team> {
    @Nullable
    public Team getTeam(@NotNull Object identifier) {
        return ChainWrappers.lambdaQueryChain(Team.class)
                .eq(Team::getId, identifier)
                .or(w -> w.eq(Team::getName, identifier))
                .one();
    }
}
