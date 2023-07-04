package com.github.bitsapling.sapling.module.torrent;

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
@TableName("torrent_comments")
public class TorrentComment implements Serializable {
    @TableId(value = "id")
    private Long id;
    @TableField("torrent")
    private Long torrent;
    @TableField("owner")
    private Long owner;
    @TableField("reply_to")
    private Long replyTo;
    @TableField("created_at")
    private LocalDateTime createdAt;
    @TableField("edited_at")
    private LocalDateTime editedAt;
    @TableField("description")
    private String description;
}
