package com.ghostchu.sapling.domain.type;

public enum PeerNetworkType {
    IPV4(0),
    IPV6(1),
    BOTH(2);

    private final int id;

    PeerNetworkType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
