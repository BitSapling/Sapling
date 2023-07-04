package com.github.bitsapling.sapling.module.failedlogin;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@TableName("`failed_logins`")
public class FailedLogin implements Serializable {
    @TableId(value = "`id`")
    private Long id;
    @TableField("`user`")
    private Long user;
    @TableField("`time`")
    private LocalDateTime time;
    @TableField("`identifier`")
    private String identifier;
    @TableField("`credential`")
    private String credential;
    @TableField("`ip`")
    private String ip;
    @TableField("`user_agent`")
    private String userAgent;
}
