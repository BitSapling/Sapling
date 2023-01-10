package com.github.bitsapling.sapling.exception;

public class InvalidTorrentVersionException extends TorrentException {

    public InvalidTorrentVersionException(String reason) {
        super(reason);
    }
}
