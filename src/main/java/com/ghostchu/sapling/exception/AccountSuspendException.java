package com.ghostchu.sapling.exception;

public class AccountSuspendException extends AnnounceException {

    public AccountSuspendException(String reason) {
        super(reason);
    }
}
