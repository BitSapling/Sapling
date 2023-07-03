package com.github.bitsapling.sapling.module.group;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.github.bitsapling.sapling.controller.ApiResponse;
import com.github.bitsapling.sapling.module.group.dto.GroupDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group")
@Slf4j
public class GroupController {
    @Autowired
    private GroupService service;

    @GetMapping("/")
    @SaCheckPermission("group:read")
    public ApiResponse<List<GroupDTO>> listingGroups() {
        return new ApiResponse<>(service.list().stream().map(group -> (GroupDTO) group).toList());
    }

    @GetMapping("/{identifier}")
    @SaCheckPermission("group:read")
    public ApiResponse<?> queryGroup(@PathVariable("identifier") String identifier) {
        Group group = service.getGroup(identifier);
        if (group != null) {
            return new ApiResponse<>((GroupDTO) group);
        } else {
            return ApiResponse.notFound();
        }
    }

    @PostMapping("/")
    @SaCheckPermission("group:write")
    public ApiResponse<?> writeGroups(@RequestBody GroupDTO groupDTO) {
        if (!service.saveOrUpdate(groupDTO)) {
            throw new IllegalStateException("Failed to write the Group to database.");
        }
        return ApiResponse.ok();
    }

    @DeleteMapping("/{identifier}")
    @SaCheckPermission("group:write")
    public ApiResponse<?> deleteGroup(@PathVariable("identifier") String identifier) {
        Group group = service.getGroup(identifier);
        if (group == null) {
            return ApiResponse.notFound();
        }
        if (!service.removeById(group)) {
            throw new IllegalStateException("Failed to delete the Group from database.");
        }
        return ApiResponse.ok();
    }


}
