package com.github.bitsapling.sapling.module.exam.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

import java.math.BigInteger;

@EqualsAndHashCode
@Validated
@Data
public class DraftedExamPlan {
    /**
     * 考核计划名称
     */
    @NotEmpty
    private String name;
    /**
     * 考核计划持续时间 （毫秒）
     */
    @Min(0)
    private BigInteger duration;
    /**
     * 考核计划目标上传量
     */
    @Min(0)
    private BigInteger targetUploaded;
    /**
     * 考核计划目标下载量
     */
    @Min(0)
    private BigInteger targetDownloaded;
    /**
     * 考核计划目标实际上传量
     */
    @Min(0)
    private BigInteger targetRealUploaded;
    /**
     * 考核计划目标实际下载量
     */
    @Min(0)
    private BigInteger targetRealDownloaded;
    /**
     * 考核计划目标魔力值
     */
    @Min(0)
    private BigInteger targetKarma;
    /**
     * 考核计划目标分享率
     */
    @Min(0)
    private BigInteger targetShareRatio;
}
