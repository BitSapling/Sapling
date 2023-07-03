package com.github.bitsapling.sapling.module.announcement;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.github.bitsapling.sapling.controller.ApiResponse;
import com.github.bitsapling.sapling.module.announcement.dto.AnnouncementDTO;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/announcement")
@Slf4j
public class AnnouncementController {
    @Autowired
    private AnnouncementService service;

    @PostMapping("/")
    @SaCheckPermission("announcement:write")
    public ApiResponse<Void> writeAnnouncement(@NotNull AnnouncementDTO createDTO) {
        if (createDTO.getAddedAt() == null) {
            createDTO.setAddedAt(LocalDateTime.now());
        }
        if (createDTO.getOwner() == null || createDTO.getOwner() == 0) {
            createDTO.setOwner(StpUtil.getLoginIdAsLong());
        }
        if (!service.saveOrUpdate(createDTO)) {
            throw new IllegalStateException("Failed to write the announcement into database");
        }
        return ApiResponse.ok();
    }

    @GetMapping("/")
    @SaCheckPermission("announcement:read")
    public ApiResponse<List<AnnouncementDTO>> listAnnouncements() {
        List<Announcement> announcements = service.getLastNAnnouncements(10);
        return new ApiResponse<>(announcements.stream().map(raw -> (AnnouncementDTO) raw).toList());
    }

    @GetMapping("/{identifier}")
    @SaCheckPermission("announcement:read")
    public ApiResponse<?> queryAnnouncement(@PathVariable("identifier") String identifier) {
        Announcement announcement = service.getById(Long.parseLong(identifier));
        if (announcement != null) {
            return new ApiResponse<>(announcement);
        } else {
            return ApiResponse.notFound();
        }
    }
}
