package com.ghostchu.sapling.exception;

public class NotAllowedClientException extends AnnounceException {
    public NotAllowedClientException(String reason) {
        super(reason);
    }
}
