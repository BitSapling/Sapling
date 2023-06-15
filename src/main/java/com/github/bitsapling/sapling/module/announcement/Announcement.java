package com.github.bitsapling.sapling.module.announcement;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("announcements")
public class Announcement implements Serializable {
    @TableId(value = "id")
    private Long id;
    @TableField("owner")
    private Long owner;
    @TableField("added_at")
    private LocalDateTime addedAt;
    @TableField("ended_at")
    private LocalDateTime endedAt;
    @TableField("title")
    private String title;
    @TableField("content")
    private String content;
}
