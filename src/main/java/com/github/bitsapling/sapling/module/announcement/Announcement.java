package com.github.bitsapling.sapling.module.announcement;

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
@TableName("announcements")
public class Announcement implements Serializable {
    /**
     * 公告主键ID
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 公告发布人 UID
     */
    @TableField("owner")
    private Long owner;
    /**
     * 公告发布于
     */
    @TableField("added_at")
    private LocalDateTime addedAt;
    /**
     * 公告结束于
     */
    @TableField("ended_at")
    private LocalDateTime endedAt;
    /**
     * 公告标题
     */
    @TableField("title")
    private String title;
    /**
     * 公告内容
     */
    @TableField("content")
    private String content;
}
