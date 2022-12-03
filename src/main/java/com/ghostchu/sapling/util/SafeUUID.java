package com.ghostchu.sapling.util;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class SafeUUID {
    @Nullable
    public static UUID fromString(String str) {
        try {
            return UUID.fromString(str);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
