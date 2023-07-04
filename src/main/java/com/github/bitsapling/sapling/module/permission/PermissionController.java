package com.github.bitsapling.sapling.module.permission;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.github.bitsapling.sapling.controller.ApiResponse;
import com.github.bitsapling.sapling.module.permission.dto.DraftedPermission;
import com.github.bitsapling.sapling.module.permission.dto.PermissionModuleDtoMapper;
import com.github.bitsapling.sapling.module.permission.dto.RegisteredPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
@Slf4j
@Tag(name = "权限")
public class PermissionController {
    private static final PermissionModuleDtoMapper DTO_MAPPER = PermissionModuleDtoMapper.INSTANCE;
    @Autowired
    private PermissionService service;

    @Operation(summary = "列出所有组的所有权限")
    @GetMapping(value = "/", produces = "application/json")
    @SaCheckPermission("permission:read")
    public ApiResponse<List<RegisteredPermission>> listingPermissions() {
        return new ApiResponse<>(service.list().stream().map(DTO_MAPPER::toRegisteredObject).toList());
    }

    @Operation(summary = "列出指定组的所有权限")
    @GetMapping(value = "/group/{identifier}", produces = "application/json")
    @SaCheckPermission("permission:read")
    public ApiResponse<List<RegisteredPermission>> listingPermissionsByGroup(@PathVariable("identifier") Long identifier) {
        return new ApiResponse<>(service.getPermissionByGroup(identifier).stream().map(DTO_MAPPER::toRegisteredObject).toList());
    }

    @Operation(summary = "查询指定权限信息")
    @GetMapping(value = "/{identifier}", produces = "application/json")
    @SaCheckPermission("permission:read")
    public ApiResponse<?> queryPermission(@PathVariable("identifier") String identifier) {
        Permission permission = service.getById(identifier);
        if (permission == null) {
            return ApiResponse.notFound();
        }
        return new ApiResponse<>(DTO_MAPPER.toRegisteredObject(permission));
    }

    @Operation(summary = "设置权限")
    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    @SaCheckPermission("permission:write")
    public ApiResponse<Void> writePermission(@RequestBody DraftedPermission draftedPermission) {
        Permission permission = new Permission(0L, draftedPermission.getGroup(), draftedPermission.getPermission(), draftedPermission.getValue());
        if (!service.saveOrUpdate(permission)) {
            throw new IllegalStateException("无法写入权限到数据库中");
        }
        log.info("权限信息已更新，用户可能需要重新登录以刷新权限缓存。");
        return ApiResponse.ok();
    }

    @Operation(summary = "更新权限")
    @PutMapping(value = "/{identifier}", consumes = "application/json", produces = "application/json")
    @SaCheckPermission("permission:write")
    public ApiResponse<Void> updatePermission(@PathVariable("identifier") String identifier, @RequestBody DraftedPermission draftedPermission) {
        Permission permission = service.getById(identifier);
        if (permission == null) {
            return ApiResponse.notFound();
        }
        permission.setGroup(draftedPermission.getGroup());
        permission.setPermission(draftedPermission.getPermission());
        permission.setValue(draftedPermission.getValue());
        if (!service.saveOrUpdate(permission)) {
            throw new IllegalStateException("无法写入权限到数据库中");
        }
        log.info("权限信息已更新，用户可能需要重新登录以刷新权限缓存。");
        return ApiResponse.ok();
    }

    @Operation(summary = "删除权限")
    @DeleteMapping(value = "/{identifier}", consumes = "application/json", produces = "application/json")
    @SaCheckPermission("permission:write")
    public ApiResponse<?> deletePermission(@PathVariable("identifier") String identifier) {
        Permission permission = service.getById(identifier);
        if (permission == null) {
            return ApiResponse.notFound();
        }
        if (!service.removeById(permission)) {
            throw new IllegalStateException("无法从数据库中删除权限");
        }
        log.info("权限信息已更新，用户可能需要重新登录以刷新权限缓存。");
        return ApiResponse.ok();
    }

}
