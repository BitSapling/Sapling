package com.github.bitsapling.sapling.module.exam.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

/**
 * 已部署的考核（用户提交修改）
 */
@EqualsAndHashCode
@Validated
@Data
public class DraftedExam {
    /**
     * 考核用户 UID
     */
    @Positive
    private Long user;
    /**
     * 考核计划主键ID
     */
    @Positive
    private Long examPlan;
    /**
     * 考核开始于
     */
    @PastOrPresent
    private LocalDateTime startedAt;
    /**
     * 考核结束于
     */
    @FutureOrPresent
    private LocalDateTime endAt;
    /**
     * 考核状态
     */
    @NotNull
    private Short status;
}
