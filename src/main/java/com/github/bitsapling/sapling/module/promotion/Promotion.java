package com.github.bitsapling.sapling.module.promotion;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@TableName("groups")
public class Promotion implements Serializable {
    @TableId(value = "id")
    private Long id;
    @TableField("name")
    private String name;
    @TableField("icon_url")
    private String iconUrl;
    @TableField("upload_multiplier")
    private BigDecimal uploadMultiplier;
    @TableField("download_multiplier")
    private BigDecimal downloadMultiplier;
    @TableField("is_default")
    private Boolean isDefault;
}
