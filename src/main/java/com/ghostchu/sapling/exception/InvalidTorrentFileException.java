package com.ghostchu.sapling.exception;

public class InvalidTorrentFileException extends TorrentException {

    public InvalidTorrentFileException(String reason) {
        super(reason);
    }
}
