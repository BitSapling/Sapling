package com.github.bitsapling.sapling.objects;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;
import java.time.Instant;
@AllArgsConstructor
@Data
public class Peer {
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
    private Instant updateAt;
    private Duration seedingTime;
}
