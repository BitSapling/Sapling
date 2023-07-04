package com.github.bitsapling.sapling.module.uacontrol;

import com.baomidou.mybatisplus.annotation.EnumValue;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public enum BtClientUAMatchType implements Serializable {
    EQUALS((short) 0),
    CONTAINS((short) 1),
    NOT_CONTAINS((short) 2),
    START_WITH((short) 3),
    END_WITH((short) 4),
    REGEX((short) 5);
    @EnumValue
    private final short value;

    BtClientUAMatchType(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }

    @Nullable
    public BtClientUAMatchType fromValue(short value) {
        for (BtClientUAMatchType matchType : BtClientUAMatchType.values()) {
            if (matchType.getValue() == value) {
                return matchType;
            }
        }
        return null;
    }
}
