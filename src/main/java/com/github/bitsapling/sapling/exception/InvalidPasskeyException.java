package com.github.bitsapling.sapling.exception;

import org.jetbrains.annotations.NotNull;

public class InvalidPasskeyException extends AnnounceException {
    public InvalidPasskeyException() {
        super("Given passkey invalid or not registered");
    }
}
