package com.github.bitsapling.sapling.module.announcement.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

/**
 * 已发布的公告内容
 */
@EqualsAndHashCode
@Validated
@Data
public class PublishedAnnouncement {
    /**
     * 公告主键ID
     */
    private Long id;
    /**
     * 公告发布人 UID
     */
    private Long owner;
    /**
     * 公告发布于
     */
    private LocalDateTime addedAt;
    /**
     * 公告结束于
     */
    private LocalDateTime endedAt;
    /**
     * 公告标题
     */
    private String title;
    /**
     * 公告内容
     */
    private String content;
}
