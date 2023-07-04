package com.github.bitsapling.sapling.module.announcement.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

/**
 * 用户提交的公告草稿
 */
@Validated
@Data
public class DraftedAnnouncement {
    /**
     * 公告结束于
     */
    private @FutureOrPresent LocalDateTime endedAt;
    /**
     * 公告标题
     */
    private @NotEmpty String title;
    /**
     * 公告内容
     */
    private @NotEmpty String content;
}
