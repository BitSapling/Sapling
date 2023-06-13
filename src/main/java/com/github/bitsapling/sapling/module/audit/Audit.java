package com.github.bitsapling.sapling.module.audit;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("audits")
public class Audit {
    @TableId(type = IdType.INPUT)
    @TableField("id")
    private Long id;
    @TableField("user")
    private Long user;
    @TableField("event_type")
    private String eventType;
    @TableField("created_at")
    private LocalDateTime createdAt;
    @TableField("ip")
    private String ip;
    @TableField("user_agent")
    private String userAgent;
    @TableField("data")
    private String description;
}
