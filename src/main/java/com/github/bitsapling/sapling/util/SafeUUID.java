package com.github.bitsapling.sapling.util;

import org.jetbrains.annotations.NotNull;
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

    @NotNull
    public static UUID nilUniqueId() {
        return new UUID(0, 0);
    }

    @NotNull
    public static String stripDashes(@NotNull String uuid) {
        return uuid.replaceAll("-", "");
    }

    @Nullable
    public static String addDashes(@NotNull String uuid) {
        if (!isUUID(uuid)) {
            return null;
        }
        return uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-" + uuid.substring(16, 20) + "-" + uuid.substring(20);
    }

    public static boolean isUUID(@NotNull String str) {
        return str.matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");
    }
}
