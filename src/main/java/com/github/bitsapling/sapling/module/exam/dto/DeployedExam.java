package com.github.bitsapling.sapling.module.exam.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

/**
 * 已部署的考核
 */
@EqualsAndHashCode
@Validated
@Data
public class DeployedExam {
    /**
     * 考核主键ID
     */
    private Long id;
    /**
     * 考核用户 UID
     */
    private Long user;
    /**
     * 考核计划主键ID
     */
    private Long examPlan;
    /**
     * 考核开始于
     */
    private LocalDateTime startedAt;
    /**
     * 考核结束于
     */
    private LocalDateTime endAt;
    /**
     * 考核状态
     */
    private Short status;
}
