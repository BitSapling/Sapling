package com.github.bitsapling.sapling.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Peer implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private  long id;
    private  String ip;
    private  int port;
    private  String infoHash;
    private  String peerId;
    private  String userAgent;
    private String passKey;
    private long uploaded;
    private long downloaded;
    private long left;
    private boolean seeder;
    private Timestamp updateAt;
    private long seedingTime;
}
