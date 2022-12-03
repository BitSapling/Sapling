package com.ghostchu.sapling.domain.type;

public enum MailVerificationStatus {
    WAITING_CONFIRM(1),
    CONFIRMED(0);
    private final int id;

    MailVerificationStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
