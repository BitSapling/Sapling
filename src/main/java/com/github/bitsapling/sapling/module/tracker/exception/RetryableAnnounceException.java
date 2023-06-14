package com.github.bitsapling.sapling.module.tracker.exception;

import org.jetbrains.annotations.NotNull;

public class RetryableAnnounceException extends AnnounceException {
    private final int retryIn;

    public RetryableAnnounceException(@NotNull String reason, int retryIn) {
        super(reason);
        this.retryIn = retryIn;
    }

    public int getRetryIn() {
        return retryIn;
    }
}