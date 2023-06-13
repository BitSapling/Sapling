package com.github.bitsapling.sapling.module.mail;

import org.jetbrains.annotations.Nullable;

public enum MailDirection {
    IN((short) 0),
    OUT((short) 1);
    private final short value;

    MailDirection(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }

    @Nullable
    public MailDirection fromValue(short value) {
        for (MailDirection direction : MailDirection.values()) {
            if (direction.getValue() == value) {
                return direction;
            }
        }
        return null;
    }
}
