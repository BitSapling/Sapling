package com.github.bitsapling.sapling.objects;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
@AllArgsConstructor
@Data
public class User  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final long id;
    private String email;
    private String passwordHash;
    private String username;
    private UserGroup group;
    private String passkey;
    private final Timestamp createdAt;
    private String avatar;
    private String customTitle;
    private String signature;
    private String language;
    private String downloadBandwidth;
    private String uploadBandwidth;
    private long downloaded;
    private long uploaded;
    private long realDownloaded;
    private long realUploaded;
    private String isp;
    private BigDecimal karma;
    private int inviteSlot;
    private long seedingTime;
}
