package com.ghostchu.sapling.domain.type;

public enum TorrentStatus {
    PENDING_REVIEW(1),
    NORMAL(0),
    BANNED(2),
    SOFT_DELETED(3);
    private final int id;

    TorrentStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
