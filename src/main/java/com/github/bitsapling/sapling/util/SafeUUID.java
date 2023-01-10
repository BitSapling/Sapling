package com.github.bitsapling.sapling.util;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Convert String to a UUID without IllegalArgumentException
 */
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
