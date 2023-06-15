package com.github.bitsapling.sapling.common;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface MapSerializable {
    @NotNull
    Map<String, Object> toMap();
}
