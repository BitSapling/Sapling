package com.ghostchu.sapling.exception;

public class TorrentNotExistException extends AnnounceException {
    public TorrentNotExistException(String reason) {
        super(reason);
    }
}
