package com.ghostchu.sapling.exception;

public class TooManyPeersException extends AnnounceException {
    public TooManyPeersException(String reason) {
        super(reason);
    }
}
