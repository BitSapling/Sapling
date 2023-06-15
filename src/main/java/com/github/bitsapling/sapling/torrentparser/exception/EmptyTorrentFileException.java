package com.github.bitsapling.sapling.torrentparser.exception;

public class EmptyTorrentFileException extends TorrentException {

    public EmptyTorrentFileException() {
        super("Torrent files tree are empty!");
    }
}