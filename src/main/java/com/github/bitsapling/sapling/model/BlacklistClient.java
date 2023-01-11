package com.github.bitsapling.sapling.model;

import org.jetbrains.annotations.NotNull;

public class BlacklistClient {
    public boolean isBanned(@NotNull String userAgent){
        // TODO: Database model
        return userAgent.contains("Mozilla");
    }
}
