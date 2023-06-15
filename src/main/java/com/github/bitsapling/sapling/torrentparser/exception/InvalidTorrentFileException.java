package com.github.bitsapling.sapling.torrentparser.exception;

public class InvalidTorrentFileException extends TorrentException {

    public InvalidTorrentFileException(String reason) {
        super(reason);
    }
}