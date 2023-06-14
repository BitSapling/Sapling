package com.github.bitsapling.sapling.module.tracker.handle;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@AllArgsConstructor
@Data
public class AnnounceTask implements Serializable {
    private final @NotNull String ip;
    private final int port;
    private final @NotNull String infoHash;
    private final byte[] peerId;
    private final long uploaded;
    private final long downloaded;
    private final long left;
    private final @NotNull AnnounceEventType event;
    private final int numWant;
    private final boolean compact;
    private final boolean noPeerId;
    private final boolean supportCrypto;
    private final int redundant;
    private final @NotNull String userAgent;
    private final @NotNull String passKey;
    private final long userId;
    private final long torrentId;
}
