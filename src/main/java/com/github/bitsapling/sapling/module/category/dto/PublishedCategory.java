package com.github.bitsapling.sapling.module.category.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

/**
 * 已发布的分类
 */
@EqualsAndHashCode
@Validated
@Data
public class PublishedCategory {
    /**
     * 分类主键ID
     */
    private Long id;
    /**
     * 分类名称
     */
    private String name;
    /**
     * 分类图标 URL
     */
    private String iconUrl;
    /**
     * 分类 CSS 类名
     */
    private String cssClassName;
    /**
     * 分类权限名称
     */
    private String permissionName;
}
