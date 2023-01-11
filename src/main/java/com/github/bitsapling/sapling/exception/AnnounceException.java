package com.github.bitsapling.sapling.exception;

import org.jetbrains.annotations.NotNull;

public class AnnounceException extends Exception{
    public AnnounceException(@NotNull String reason) {
        super(reason);
    }
}
