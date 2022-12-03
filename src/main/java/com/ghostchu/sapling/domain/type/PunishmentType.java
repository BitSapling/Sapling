package com.ghostchu.sapling.domain.type;

public enum PunishmentType {
    WARNING(0),
    MUTE(1),
    DISABLE_DOWNLOAD_PRIVILEGE(2),
    DISABLE_ACCOUNT(3),
    BAN(4),
    PUNISHMENT_EXPIRED(-1),
    REMOVE_PUNISHMENT(-2);
    private int id = 0;

    PunishmentType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
