package com.github.bitsapling.sapling.util;

import org.jetbrains.annotations.Nullable;

public class MiscUtil {
    @SafeVarargs
    @Nullable
    public static <T> T anyNotNull(T... objects) {
        for (T object : objects) {
            if (object != null) {
                return object;
            }
        }
        return null;
    }
}
