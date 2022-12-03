package com.ghostchu.sapling.exception;

public class PeerBannedException extends AnnounceException {
    public PeerBannedException(String reason) {
        super(reason);
    }
}
