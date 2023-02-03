package com.github.bitsapling.sapling.type;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public enum AnnounceEventType {
    STARTED("started"),
    COMPLETED("completed"),
    STOPPED("stopped"),
    PAUSED("paused"),

    UNKNOWN("unknown");
    private final String key;
    AnnounceEventType(String key){
        this.key = key;
    }

    @NotNull
    public String getKey() {
        return key;
    }
    public static @NotNull AnnounceEventType fromName(@Nullable String name){
        if(name == null) return UNKNOWN;
        name = name.toLowerCase(Locale.ROOT);
        for (AnnounceEventType type : values()){
            if (type.getKey().equals(name)){
                return type;
            }
        }
        return UNKNOWN;
    }
}
