package com.ghostchu.sapling.domain.type;

public enum PrivacyLevel {
    NORMAL(0),
    LOW(1),
    HIGH(2);
    private final int id;

    PrivacyLevel(int id) {
        this.id = id;
    }
}
