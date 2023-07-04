package com.github.bitsapling.sapling.module.group;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.github.bitsapling.sapling.controller.ApiResponse;
import com.github.bitsapling.sapling.module.group.dto.DraftedGroup;
import com.github.bitsapling.sapling.module.group.dto.GroupModuleDtoMapper;
import com.github.bitsapling.sapling.module.group.dto.PublishedGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group")
@Slf4j
@Tag(name = "权限组")
public class GroupController {
    private static final GroupModuleDtoMapper DTO_MAPPER = GroupModuleDtoMapper.INSTANCE;
    @Autowired
    private GroupService service;

    @Operation(summary = "获取所有权限组")
    @GetMapping(value = "/", produces = "application/json")
    @SaCheckPermission("group:read")
    public ApiResponse<List<PublishedGroup>> listingGroups() {
        return new ApiResponse<>(service.list().stream().map(DTO_MAPPER::toDeployedObject).toList());
    }

    @Operation(summary = "获取指定权限组的详细信息")
    @GetMapping(value = "/{identifier}", produces = "application/json")
    @SaCheckPermission("group:read")
    public ApiResponse<?> queryGroup(@PathVariable("identifier") String identifier) {
        Group group = service.getGroup(identifier);
        if (group != null) {
            return new ApiResponse<>(DTO_MAPPER.toDeployedObject(group));
        } else {
            return ApiResponse.notFound();
        }
    }

    @Operation(summary = "创建新的权限组")
    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    @SaCheckPermission("group:write")
    public ApiResponse<?> createGroup(@RequestBody DraftedGroup draftedGroup) {
        Group group = new Group(0L,
                draftedGroup.getName(),
                draftedGroup.getIconUrl(),
                draftedGroup.getCssClassName(),
                draftedGroup.getPromotion());
        if (!service.save(group)) {
            throw new IllegalStateException("无法将权限组保存到数据库");
        }
        return ApiResponse.ok();
    }

    @Operation(summary = "更新指定权限组的详细信息")
    @PutMapping(value = "/{identifier}", consumes = "application/json", produces = "application/json")
    @SaCheckPermission("group:write")
    public ApiResponse<?> updateGroup(@PathVariable("identifier") String identifier, @RequestBody DraftedGroup draftedGroup) {
        Group group = service.getGroup(identifier);
        if (group == null) {
            return ApiResponse.notFound();
        }
        group.setName(draftedGroup.getName());
        group.setIconUrl(draftedGroup.getIconUrl());
        group.setCssClassName(draftedGroup.getCssClassName());
        group.setPromotion(draftedGroup.getPromotion());
        if (!service.updateById(group)) {
            throw new IllegalStateException("无法将权限组更新到数据库");
        }
        return ApiResponse.ok();
    }

    @Operation(summary = "删除指定权限组")
    @DeleteMapping(value = "/{identifier}", consumes = "application/json", produces = "application/json")
    @SaCheckPermission("group:write")
    public ApiResponse<?> deleteGroup(@PathVariable("identifier") String identifier) {
        Group group = service.getGroup(identifier);
        if (group == null) {
            return ApiResponse.notFound();
        }
        if (!service.removeById(group)) {
            throw new IllegalStateException("无法将权限组从数据库中删除");
        }
        return ApiResponse.ok();
    }
}
