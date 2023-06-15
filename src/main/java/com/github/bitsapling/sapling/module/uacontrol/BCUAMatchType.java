package com.github.bitsapling.sapling.module.uacontrol;

import com.baomidou.mybatisplus.annotation.EnumValue;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public enum BCUAMatchType implements Serializable {
    EQUALS((short) 0),
    CONTAINS((short) 1),
    NOT_CONTAINS((short) 2),
    START_WITH((short) 3),
    END_WITH((short) 4),
    REGEX((short) 5);
    @EnumValue
    private final short value;

    BCUAMatchType(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }

    @Nullable
    public BCUAMatchType fromValue(short value) {
        for (BCUAMatchType matchType : BCUAMatchType.values()) {
            if (matchType.getValue() == value) {
                return matchType;
            }
        }
        return null;
    }
}
