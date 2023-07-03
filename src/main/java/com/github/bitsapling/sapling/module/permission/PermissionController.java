package com.github.bitsapling.sapling.module.permission;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.github.bitsapling.sapling.controller.ApiResponse;
import com.github.bitsapling.sapling.module.permission.dto.PermissionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
@Slf4j
public class PermissionController {
    @Autowired
    private PermissionService service;

    @GetMapping("/")
    @SaCheckPermission("permission:read")
    public ApiResponse<List<PermissionDTO>> listingPermissions() {
        return new ApiResponse<>(service.list().stream().map(permission -> (PermissionDTO) permission).toList());
    }

    @GetMapping("/group/{identifier}")
    @SaCheckPermission("permission:read")
    public ApiResponse<List<PermissionDTO>> listingPermissionsByGroup(@PathVariable("identifier") Long identifier) {
        return new ApiResponse<>(service.getPermissionByGroup(identifier).stream().map(permission -> (PermissionDTO) permission).toList());
    }

    @GetMapping("/{identifier}")
    @SaCheckPermission("permission:read")
    public ApiResponse<?> queryPermission(@PathVariable("identifier") String identifier) {
        Permission permission = service.getPermission(identifier);
        if (permission != null) {
            return new ApiResponse<>((PermissionDTO) permission);
        } else {
            return ApiResponse.notFound();
        }
    }

    @PostMapping("/")
    @SaCheckPermission("permission:write")
    public ApiResponse<Void> writePermission(@RequestBody PermissionDTO permissionDTO) {
        if (!service.saveOrUpdate(permissionDTO)) {
            throw new IllegalStateException("Failed to write the Permission to database.");
        }
        log.info("Permissions has been updated, users may need re-login to apply changes.");
        return ApiResponse.ok();
    }

    @DeleteMapping("/{identifier}")
    @SaCheckPermission("permission:write")
    public ApiResponse<?> deletePermission(@PathVariable("identifier") String identifier) {
        Permission permission = service.getPermission(identifier);
        if (permission == null) {
            return ApiResponse.notFound();
        }
        if (!service.removeById(permission)) {
            throw new IllegalStateException("Failed to delete the Permission from database.");
        }
        log.info("Permissions has been updated, users may need re-login to apply changes.");
        return ApiResponse.ok();
    }

}
