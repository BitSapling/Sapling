package com.github.bitsapling.sapling.module.category;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@TableName("`categories`")
public class Category implements Serializable {
    @TableId(value = "id")
    private Long id;
    @TableField("`name`")
    private String name;
    @TableField("`icon_url`")
    private String iconUrl;
    @TableField("`css_class_name`")
    private String cssClassName;
    @TableField("`permission_name`")
    private String permissionName;
}
