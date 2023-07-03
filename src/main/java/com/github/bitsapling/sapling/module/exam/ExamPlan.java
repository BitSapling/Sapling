package com.github.bitsapling.sapling.module.exam;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigInteger;

@Data
@AllArgsConstructor
@Accessors(chain = true)
@TableName("exams_plans")
public class ExamPlan implements Serializable {
    @TableId(value = "id")
    private Long id;
    @TableField("name")
    private String name;
    @TableField("duration")
    private BigInteger duration;
    @TableField("target_uploaded")
    private BigInteger targetUploaded;
    @TableField("target_downloaded")
    private BigInteger targetDownloaded;
    @TableField("target_real_uploaded")
    private BigInteger targetRealUploaded;
    @TableField("target_real_downloaded")
    private BigInteger targetRealDownloaded;
    @TableField("target_karma")
    private BigInteger targetKarma;
    @TableField("target_share_ratio")
    private BigInteger targetShareRatio;
}
