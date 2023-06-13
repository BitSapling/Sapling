package com.github.bitsapling.sapling.module.tag;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("tags")
public class Tag {
    @TableField("id")
    private Long id;
    @TableField("name")
    private String name;
}
