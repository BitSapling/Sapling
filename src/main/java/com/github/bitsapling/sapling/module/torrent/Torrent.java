package com.github.bitsapling.sapling.module.torrent;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("torrents")
public class Torrent implements Serializable {
    @TableId(value = "id")
    private Long id;
    @TableField("info_hash_v1")
    private String infoHashV1;
    @TableField("info_hash_v2")
    private String infoHashV2;
    @TableField("uploader")
    private Long uploader;
    @TableField("file_id")
    private Long fileId;
    @TableField("is_review")
    private Boolean isReview;
    @TableField("is_banned")
    private Boolean isBanned;
    @TableField("is_draft")
    private Boolean isDraft;
    @TableField("added_at")
    private LocalDateTime addedAt;
    @TableField("anonymous")
    private Boolean anonymous;
    @TableField("title")
    private String title;
    @TableField("subtitle")
    private String subTitle;
    @TableField("category")
    private Long category;
    @TableField("description")
    private String description;
    @TableField("promotion")
    private Long promotion;
    @TableField("promotion_end_at")
    private LocalDateTime promotionEndAt;
}
