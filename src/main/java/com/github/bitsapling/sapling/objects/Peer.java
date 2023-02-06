package com.github.bitsapling.sapling.objects;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
@AllArgsConstructor
@Data
public class Peer implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final long id;
    private final String ip;
    private final int port;
    private final String infoHash;
    private final String peerId;
    private final String userAgent;
    private String passKey;
    private long uploaded;
    private long downloaded;
    private long left;
    private boolean seeder;
    private Timestamp updateAt;
    private long seedingTime;
}
