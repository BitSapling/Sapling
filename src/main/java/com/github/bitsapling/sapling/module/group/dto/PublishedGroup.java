package com.github.bitsapling.sapling.module.group.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

@EqualsAndHashCode
@Validated
@Data
public class PublishedGroup {
    /**
     * 权限组主键ID
     */
    private Long id;
    /**
     * 权限组名称
     */
    private String name;
    /**
     * 权限组图标URL
     */
    private String iconUrl;
    /**
     * 权限组CSS类名
     */
    private String cssClassName;
    /**
     * 权限组种子优惠策略ID
     */
    private Long promotion;
}
