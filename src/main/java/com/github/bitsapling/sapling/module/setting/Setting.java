package com.github.bitsapling.sapling.module.setting;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@TableName("settings")
public class Setting implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    @TableField("key")
    private String key;
    @TableField("value")
    private String value;
}
