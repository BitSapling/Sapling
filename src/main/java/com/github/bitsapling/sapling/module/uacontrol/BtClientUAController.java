package com.github.bitsapling.sapling.module.uacontrol;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.github.bitsapling.sapling.controller.ApiResponse;
import com.github.bitsapling.sapling.module.uacontrol.dto.BtClientUADTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/uacontrol")
@Slf4j
public class BtClientUAController {
    @Autowired
    private BtClientUAService service;

    @GetMapping("/")
    @SaCheckPermission("uacontrol:read")
    public ApiResponse<?> listUaRules() {
        return new ApiResponse<>(service.getAllRules(false).stream().map(ua -> (BtClientUADTO) ua).toList());
    }

    @GetMapping("/{identifier}")
    @SaCheckPermission("uacontrol:read")
    public ApiResponse<?> queryUaRule(@PathVariable("identifier") String identifier) {
        BtClientUA ua = service.getById(identifier);
        if (ua != null) {
            return new ApiResponse<>((BtClientUADTO) ua);
        } else {
            return ApiResponse.notFound();
        }
    }

    @PostMapping("/")
    @SaCheckPermission("uacontrol:write")
    public ApiResponse<Void> writeUaRule(@RequestBody BtClientUADTO uaDTO) {
        if (!service.saveOrUpdate(uaDTO)) {
            throw new IllegalStateException("Failed to write the ua rule to database.");
        }
        return ApiResponse.ok();
    }

    @DeleteMapping("/{identifier}")
    @SaCheckPermission("uacontrol:write")
    public ApiResponse<Void> deleteUaRule(@PathVariable("identifier") String identifier) {
        BtClientUA ua = service.getById(identifier);
        if (ua == null) {
            return ApiResponse.notFound();
        }
        if (!service.removeById(ua)) {
            throw new IllegalStateException("Failed to delete the ua rule from database.");
        }
        return ApiResponse.ok();
    }
}
