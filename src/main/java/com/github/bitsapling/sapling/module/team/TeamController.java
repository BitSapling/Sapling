package com.github.bitsapling.sapling.module.team;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.github.bitsapling.sapling.controller.ApiResponse;
import com.github.bitsapling.sapling.module.team.dto.TeamDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/team")
@Slf4j
public class TeamController {
    @Autowired
    private TeamService service;

    @GetMapping("/")
    @SaCheckPermission("team:read")
    public ApiResponse<List<TeamDTO>> listTeams() {
        return new ApiResponse<>(service.list().stream().map(team -> (TeamDTO) team).toList());
    }

    @GetMapping("/{identifier}")
    @SaCheckPermission("team:read")
    public ApiResponse<?> queryTeam(@PathVariable("identifier") String identifier) {
        Team team = service.getTeam(identifier);
        if (team != null) {
            return new ApiResponse<>((TeamDTO) team);
        } else {
            return ApiResponse.notFound();
        }
    }

    @PostMapping("/")
    @SaCheckPermission("team:write")
    public ApiResponse<Void> writeTeam(@RequestBody TeamDTO teamDTO) {
        if (!service.saveOrUpdate(teamDTO)) {
            throw new IllegalStateException("Failed to write the team to database.");
        }
        return ApiResponse.ok();
    }

    @DeleteMapping("/{identifier}")
    @SaCheckPermission("team:write")
    public ApiResponse<Void> deleteTeam(@PathVariable("identifier") String identifier) {
        Team team = service.getTeam(identifier);
        if (team == null) {
            return ApiResponse.notFound();
        }
        if (!service.removeById(team)) {
            throw new IllegalStateException("Failed to delete the team from database.");
        }
        return ApiResponse.ok();
    }
}
