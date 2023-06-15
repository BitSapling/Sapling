package com.github.bitsapling.sapling.module.uacontrol;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@TableName("announcements")
public class BtClientUA implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    @TableField("user_agent")
    private String userAgent;
    @TableField("match_type")
    private BCUAMatchType matchType;
    @TableField("is_enabled")
    private Boolean enabled;
    @TableField("description")
    private String description;
}
