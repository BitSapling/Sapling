package com.github.bitsapling.sapling.exception;

import org.jetbrains.annotations.NotNull;

public class InvalidAnnounceException extends AnnounceException {
    public InvalidAnnounceException(@NotNull String reason) {
        super(reason);
    }
}
