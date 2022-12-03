package com.ghostchu.sapling.exception;

public class AnnounceCooldownException extends AnnounceException {
    public AnnounceCooldownException(String reason) {
        super(reason);
    }
}
