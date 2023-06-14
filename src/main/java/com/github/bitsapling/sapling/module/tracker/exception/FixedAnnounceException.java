package com.github.bitsapling.sapling.module.tracker.exception;

import org.jetbrains.annotations.NotNull;

public class FixedAnnounceException extends AnnounceException {
    public FixedAnnounceException(@NotNull String reason) {
        super(reason);
    }
}