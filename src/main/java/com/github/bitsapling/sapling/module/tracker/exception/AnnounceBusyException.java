package com.github.bitsapling.sapling.module.tracker.exception;

public class AnnounceBusyException extends RetryableAnnounceException {
    public AnnounceBusyException() {
        super("Server is busy for handling announce, try again later", 180);
    }
}