package com.github.bitsapling.sapling.module.team;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {
    @Autowired
    private TeamMapper mapper;

    @NotNull
    public List<Team> getAllTeams() {
        return mapper.selectList(Wrappers.lambdaQuery(Team.class));
    }

    @Nullable
    public Team getTeam(@NotNull Long id) {
        return mapper.selectById(id);
    }

    @Nullable
    public Team getTeam(@NotNull String name) {
        LambdaQueryWrapper<Team> wrapper = Wrappers
                .lambdaQuery(Team.class)
                .eq(Team::getName, name);
        return mapper.selectOne(wrapper);
    }

    public int addTeam(@NotNull Team team) {
        return mapper.insert(team);
    }

    public int deleteTeam(@NotNull Team team) {
        return mapper.deleteById(team.getId());
    }

    public int deleteTeam(@NotNull Long id) {
        return mapper.deleteById(id);
    }

    public int updateTeam(@NotNull Team team) {
        return mapper.updateById(team);
    }
}
