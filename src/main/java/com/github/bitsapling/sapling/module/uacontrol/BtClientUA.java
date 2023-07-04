package com.github.bitsapling.sapling.module.uacontrol;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Accessors(chain = true)
@TableName("ua_control")
public class BtClientUA implements Serializable {
    @TableId(value = "`id`")
    private Long id;
    @TableField("`user_agent`")
    private String userAgent;
    @TableField("`match_type`")
    private BtClientUAMatchType matchType;
    @TableField("`is_enabled`")
    private Boolean enabled;
    @TableField("`description`")
    private String description;
}
