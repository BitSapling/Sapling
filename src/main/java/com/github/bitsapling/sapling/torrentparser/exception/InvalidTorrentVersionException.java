package com.github.bitsapling.sapling.torrentparser.exception;

public class InvalidTorrentVersionException extends TorrentException {

    public InvalidTorrentVersionException(String reason) {
        super(reason);
    }
}