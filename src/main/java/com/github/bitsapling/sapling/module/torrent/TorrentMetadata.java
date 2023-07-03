package com.github.bitsapling.sapling.module.torrent;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@TableName("torrent_metadata")
public class TorrentMetadata implements Serializable {
    @TableId(value = "id")
    private Long id;
    @TableField("torrent_id")
    private Long torrent;
    @TableField("seeders")
    private Long seeders;
    @TableField("leechers")
    private Long leechers;
    @TableField("times_completed")
    private Long timesCompleted;
    @TableField("times_file_downloaded")
    private Long timesFileDownloaded;
}
