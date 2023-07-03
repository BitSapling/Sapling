package com.github.bitsapling.sapling.module.setting;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.math.BigDecimal;

@Accessors(chain = true)
@TableName("settings")
public class Setting implements Serializable {
    @TableId(value = "id")
    private Long id;
    @TableField("key")
    private String key;
    @TableField("value")
    private String value;

    public Long getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public Long getValueAsLong(@Nullable Long def) {
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            return def;
        }
    }

    public Integer getValueAsInteger(@Nullable Integer def) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return def;
        }
    }

    public Boolean getValueAsBoolean(@Nullable Boolean def) {
        try {
            return Boolean.parseBoolean(value);
        } catch (Exception e) {
            return def;
        }
    }

    public Double getValueAsDouble(@Nullable Double def) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return def;
        }
    }

    public Float getValueAsFloat(@Nullable Float def) {
        try {
            return Float.parseFloat(value);
        } catch (Exception e) {
            return def;
        }
    }

    public BigDecimal getValueAsBigDecimal(@Nullable BigDecimal def) {
        try {
            return new BigDecimal(value);
        } catch (Exception e) {
            return def;
        }
    }
}
