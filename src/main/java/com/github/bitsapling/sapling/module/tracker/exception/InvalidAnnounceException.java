package com.github.bitsapling.sapling.module.tracker.exception;

import org.jetbrains.annotations.NotNull;

public class InvalidAnnounceException extends FixedAnnounceException {
    public InvalidAnnounceException(@NotNull String reason) {
        super(reason);
    }
}