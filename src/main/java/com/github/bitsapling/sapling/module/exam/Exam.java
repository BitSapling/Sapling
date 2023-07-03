package com.github.bitsapling.sapling.module.exam;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@TableName("exams")
public class Exam implements Serializable {
    @TableId(value = "id")
    private Long id;
    @TableField("user")
    private Long user;
    @TableField("exam_plan")
    private Long examPlan;
    @TableField("started_at")
    private LocalDateTime startedAt;
    @TableField("end_at")
    private LocalDateTime endAt;
    @TableField("status")
    private Short status;
}
