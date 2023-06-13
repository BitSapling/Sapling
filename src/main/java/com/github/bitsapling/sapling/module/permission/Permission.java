package com.github.bitsapling.sapling.module.permission;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@TableName("permissions")
public class Permission implements Serializable {
    @TableId(type = IdType.INPUT)
    @TableField("id")
    private Long id;
    @TableField("group")
    private Long group;
    @TableField("permission")
    private String permission;
    @TableField("value")
    private Boolean value;
}
