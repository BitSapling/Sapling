package com.github.bitsapling.sapling.module.mail.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

/**
 * 站内信（摘要）
 */
@EqualsAndHashCode
@Data
@Validated
public class PublishedMailSummary {
    /**
     * 站内信主键ID
     */
    private Long id;
    /**
     * 站内信所有者 UID
     */
    private Long owner;
    /**
     * 站内信发送者 UID
     */
    private Long sender;
    /**
     * 站内信发送者名称
     */
    private String senderName;
    /**
     * 站内信标题
     */
    private String title;
    /**
     * 站内信描述
     */
    private String description;
    /**
     * 站内信发送时间
     */
    private LocalDateTime createdAt;
    /**
     * 站内信首次阅读时间
     */
    private LocalDateTime readedAt;
}
