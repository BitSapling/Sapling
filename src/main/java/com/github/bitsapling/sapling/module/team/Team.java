package com.github.bitsapling.sapling.module.team;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("teams")
public class Team {
    @TableField("id")
    private Long id;
    @TableField("name")
    private String name;
    @TableField("permission_name")
    private String permissionName;
}
