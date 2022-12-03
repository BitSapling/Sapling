package com.ghostchu.sapling.domain.type;

public enum PeerType {
    SEEDING(0),
    LEECHING(1);
    private final int id;

    PeerType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
