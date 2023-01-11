package com.github.bitsapling.sapling.exception;

import org.jetbrains.annotations.NotNull;

public class FixedAnnounceException extends AnnounceException {
    public FixedAnnounceException(@NotNull String reason) {
        super(reason);
    }
}
