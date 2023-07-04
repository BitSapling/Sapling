package com.github.bitsapling.sapling.module.tag;

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
@TableName("`tags`")
public class Tag implements Serializable {
    @TableId(value = "`id`")
    private Long id;
    @TableField("`name`")
    private String name;
}
