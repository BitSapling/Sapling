package com.github.bitsapling.sapling.module.category.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

/**
 * 分类（用户提交）
 */
@EqualsAndHashCode
@Validated
@Data
public class DraftedCategory {
    /**
     * 分类名称
     */
    @NotEmpty
    private String name;
    /**
     * 分类图标 URL
     */
    @NotEmpty
    private String iconUrl;
    /**
     * 分类 CSS 类名
     */
    @NotEmpty
    private String cssClassName;
    /**
     * 分类权限名称
     */
    @NotEmpty
    private String permissionName;
}
