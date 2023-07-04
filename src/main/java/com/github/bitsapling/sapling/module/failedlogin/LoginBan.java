package com.github.bitsapling.sapling.module.failedlogin;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@TableName("`login_bans`")
public class LoginBan {
    @TableId(value = "`id`")
    private Long id;
    @TableField("`ip`")
    private String ip;
    @TableField("`end_time`")
    private LocalDateTime endTime;
}
