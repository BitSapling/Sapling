package com.github.bitsapling.sapling.type;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public enum AnnounceEventType {
    STARTED("started"),
    COMPLETED("completed"),
    STOPPED("stopped");
    private final String key;
    AnnounceEventType(String key){
        this.key = key;
    }

    @NotNull
    public String getKey() {
        return key;
    }
    @Nullable
    public AnnounceEventType fromName(@NotNull String name){
        name = name.toLowerCase(Locale.ROOT);
        for (AnnounceEventType type : values()){
            if (type.getKey().equals(name)){
                return type;
            }
        }
        return null;
    }
}
