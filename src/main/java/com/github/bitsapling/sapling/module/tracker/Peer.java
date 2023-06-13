package com.github.bitsapling.sapling.module.tracker;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.bitsapling.sapling.module.torrent.Torrent;
import com.github.bitsapling.sapling.module.user.User;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("peers")
public class Peer {
    @TableId(type = IdType.INPUT)
    @TableField("id")
    private Long id;
    @TableField("torrent_id")
    private Torrent torrent;
    @TableField("peer_id")
    private byte[] peerId;
    @TableField("user_id")
    private User user;
    @TableField("ip")
    private String ip;
    @TableField("port")
    private Short port;
    @TableField("uploaded")
    private BigInteger uploaded;
    @TableField("downloaded")
    private BigInteger downloaded;
    @TableField("to_go")
    private BigInteger toGo;
    @TableField("started_at")
    private LocalDateTime startedAt;
    @TableField("last_action")
    private LocalDateTime lastAction;
    @TableField("prev_action")
    private LocalDateTime prevAction;
    @TableField("user_agent")
    private String userAgent;
    @TableField("finished_at")
    private LocalDateTime finishedAt;
    @TableField("downloaded_offset")
    private BigInteger downloadedOffset;
    @TableField("uploaded_offset")
    private BigInteger uploadedOffset;
    @TableField("connectable")
    private Boolean connectable;
}
