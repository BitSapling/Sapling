package com.github.bitsapling.sapling.module.torrent;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigInteger;

@Data
@Accessors(chain = true)
@TableName("torrent_metadata")
public class UserTorrentMetadata implements Serializable {
    @TableId(value = "id")
    private Long id;
    @TableField("user")
    private Long user;
    @TableField("torrent")
    private Long torrent;
    @TableField("downloaded")
    private BigInteger downloaded;
    @TableField("uploaded")
    private BigInteger uploaded;
    @TableField("real_downloaded")
    private BigInteger realDownloaded;
    @TableField("real_uploaded")
    private BigInteger realUploaded;
    @TableField("seeding_time")
    private BigInteger seedingTime;
    @TableField("downloading_time")
    private BigInteger downloadingTime;

}
