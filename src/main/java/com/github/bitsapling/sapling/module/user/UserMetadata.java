package com.github.bitsapling.sapling.module.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@Accessors(chain = true)
@TableName("user_metadata")
public class UserMetadata implements Serializable {
    @TableId(type = IdType.INPUT)
    @TableField("id")
    private Long id;
    @TableField("user")
    private Long user;
    @TableField("downloaded")
    private BigInteger downloaded;
    @TableField("uploaded")
    private BigInteger uploaded;
    @TableField("real_downloaded")
    private BigInteger realDownloaded;
    @TableField("real_uploaded")
    private BigInteger realUploaded;
    @TableField("karma")
    private BigDecimal karma;
    @TableField("total_seeding_time")
    private BigInteger totalSeedingTime;
    @TableField("total_downloading_time")
    private BigInteger totalDownloadingTime;
}
