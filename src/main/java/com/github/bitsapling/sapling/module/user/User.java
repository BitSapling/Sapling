package com.github.bitsapling.sapling.module.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Accessors(chain = true)
@TableName("users")
public class User implements Serializable {
    @TableId(value = "`id`")
    private Long id;
    @TableField("`passkey`")
    private String passkey;
    @TableField("`username`")
    private String username;
    @TableField("`nickname`")
    private String nickname;
    @TableField("`password`")
    private String password;
    @TableField("`login_provider`")
    private Long loginProvider;
    @TableField("`login_identifier`")
    private String loginIdentifier;
    @TableField("`email`")
    private String email;
    @TableField("`email_confirmed`")
    private Boolean email_confirmed;
    @TableField("`group`")
    private Long group;
    @TableField("`avatar_url`")
    private String avatarUrl;
    @TableField("`joined_at`")
    private LocalDateTime joinedAt;
    @TableField("`last_seen_at`")
    private LocalDateTime lastSeenAt;
    @TableField("`register_ip`")
    private String registerIp;
    @TableField("`site_lang`")
    private String siteLang;
    @TableField("`bio`")
    private String bio;
    @TableField("`is_banned`")
    private Boolean isBanned;
    @TableField("`preferences`")
    private Byte[] preferences;
}
