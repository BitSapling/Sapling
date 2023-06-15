package com.github.bitsapling.sapling.torrentparser.exception;

public class InvalidTorrentPiecesException extends TorrentException {
    private final int piecesLength;

    public InvalidTorrentPiecesException(int piecesLength) {
        super("Invalid torrent pieces: " + piecesLength);
        this.piecesLength = piecesLength;
    }

    public int getPiecesLength() {
        return piecesLength;
    }
}