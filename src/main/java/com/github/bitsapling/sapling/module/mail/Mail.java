package com.github.bitsapling.sapling.module.mail;

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
@TableName("`mails`")
public class Mail implements Serializable {
    @TableId(value = "`id`")
    private Long id;
    @TableField("`owner`")
    private Long owner;
    @TableField("`sender`")
    private Long sender;
    @TableField("`sender_name`")
    private String senderName;
    @TableField("`title`")
    private String title;
    @TableField("`description`")
    private String description;
    @TableField("`created_at`")
    private LocalDateTime createdAt;
    @TableField("`readed_at`")
    private LocalDateTime readedAt;
    @TableField("`deleted_at`")
    private LocalDateTime deletedAt;

}
