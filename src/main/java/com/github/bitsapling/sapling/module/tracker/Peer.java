package com.github.bitsapling.sapling.module.tracker;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("peers")
public class Peer implements Serializable {
    @TableId(value = "id")
    private Long id;
    @TableField("torrent_id")
    private Long torrent;
    @TableField("peer_id")
    private Byte[] peerId;
    @TableField("user_id")
    private Long user;
    @TableField("ip")
    private String ip;
    @TableField("port")
    private Integer port;
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
