package com.github.bitsapling.sapling.module.group.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

@EqualsAndHashCode
@Validated
@Data
public class DraftedGroup {
    /**
     * 权限组名称
     */
    @NotEmpty
    private String name;
    /**
     * 权限组图标URL
     */
    @NotEmpty
    private String iconUrl;
    /**
     * 权限组CSS类名
     */
    @NotEmpty
    private String cssClassName;
    /**
     * 权限组种子优惠策略ID
     */
    @Positive
    private Long promotion;
    /**
     * 权限组父组
     */
    private long extend;
}
