package com.github.bitsapling.sapling.module.team;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@TableName("teams")
public class Team implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    @TableField("name")
    private String name;
    @TableField("permission_name")
    private String permissionName;
}
