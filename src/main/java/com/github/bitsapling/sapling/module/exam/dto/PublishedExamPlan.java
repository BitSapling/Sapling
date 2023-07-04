package com.github.bitsapling.sapling.module.exam.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

import java.math.BigInteger;

/**
 * 已发布的考核计划
 */
@EqualsAndHashCode
@Validated
@Data
public class PublishedExamPlan {
    /**
     * 考核计划ID
     */
    private Long id;
    /**
     * 考核计划名称
     */
    private String name;
    /**
     * 考核计划持续时间 （毫秒）
     */
    private BigInteger duration;
    /**
     * 考核计划目标上传量
     */
    private BigInteger targetUploaded;
    /**
     * 考核计划目标下载量
     */
    private BigInteger targetDownloaded;
    /**
     * 考核计划目标实际上传量
     */
    private BigInteger targetRealUploaded;
    /**
     * 考核计划目标实际下载量
     */
    private BigInteger targetRealDownloaded;
    /**
     * 考核计划目标魔力值
     */
    private BigInteger targetKarma;
    /**
     * 考核计划目标分享率
     */
    private BigInteger targetShareRatio;
}
