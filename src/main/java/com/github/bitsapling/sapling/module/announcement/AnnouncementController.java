package com.github.bitsapling.sapling.module.announcement;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.github.bitsapling.sapling.controller.ApiResponse;
import com.github.bitsapling.sapling.module.announcement.dto.AnnouncementModuleMapper;
import com.github.bitsapling.sapling.module.announcement.dto.DraftedAnnouncement;
import com.github.bitsapling.sapling.module.announcement.dto.PublishedAnnouncement;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 公告 API 端点
 */
@RestController
@RequestMapping("/announcement")
@Slf4j
@Tag(name = "公告")
public class AnnouncementController {
    private static final AnnouncementModuleMapper DTO_MAPPER = AnnouncementModuleMapper.INSTANCE;
    @Autowired
    private AnnouncementService service;

    @Operation(summary = "发布或者更新公告")
    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    @SaCheckPermission("announcement:write")
    public ApiResponse<Void> writeAnnouncement(@Valid @RequestBody DraftedAnnouncement draftedAnnouncement) {
        Announcement announcement = new Announcement(
                0L,
                StpUtil.getLoginIdAsLong(),
                LocalDateTime.now(),
                draftedAnnouncement.getEndedAt(),
                draftedAnnouncement.getTitle(),
                draftedAnnouncement.getContent());
        if (!service.save(announcement)) {
            throw new IllegalStateException("无法保存 公告到数据库中");
        }
        return ApiResponse.ok();
    }


    @Operation(summary = "获取公告列表")
    @GetMapping(value = "/", produces = "application/json")
    @SaCheckPermission("announcement:read")
    public ApiResponse<List<PublishedAnnouncement>> listAnnouncements() {
        List<Announcement> announcements = service.getLastNAnnouncements(10);
        return new ApiResponse<>(announcements.stream().map(DTO_MAPPER::toPublishedObject).toList());
    }

    @Operation(summary = "阅读指定公告详情")
    @GetMapping(value = "/{identifier}", produces = "application/json")
    @SaCheckPermission("announcement:read")
    public ApiResponse<?> queryAnnouncement(@PathVariable("identifier") String identifier) {
        Announcement announcement = service.getById(Long.parseLong(identifier));
        if (announcement != null) {
            return new ApiResponse<>(DTO_MAPPER.toPublishedObject(announcement));
        } else {
            return ApiResponse.notFound();
        }
    }

    @Operation(summary = "更新指定公告内容")
    @PutMapping(value = "/{identifier}", consumes = "application/json", produces = "application/json")
    @SaCheckPermission("announcement:write")
    public ApiResponse<Void> updateAnnouncement(@PathVariable("identifier") String identifier, @Valid @RequestBody DraftedAnnouncement draftedAnnouncement) {
        Announcement announcement = new Announcement(
                Long.parseLong(identifier),
                StpUtil.getLoginIdAsLong(),
                LocalDateTime.now(),
                draftedAnnouncement.getEndedAt(),
                draftedAnnouncement.getTitle(),
                draftedAnnouncement.getContent());
        if (!service.updateById(announcement)) {
            throw new IllegalStateException("无法更新 公告到数据库中");
        }
        return ApiResponse.ok();
    }

    @Operation(summary = "删除指定公告")
    @DeleteMapping(value = "/{identifier}", produces = "application/json")
    @SaCheckPermission("announcement:write")
    public ApiResponse<Void> deleteAnnouncement(@PathVariable("identifier") String identifier) {
        Announcement announcement = service.getById(Long.parseLong(identifier));
        if (announcement == null) {
            return ApiResponse.notFound();
        }
        if (!service.removeById(announcement)) {
            throw new IllegalStateException("无法从数据库中删除指定公告");
        }
        return ApiResponse.ok();
    }
}
