package com.github.bitsapling.sapling.exception;

public class TrackerException extends Exception {
    public TrackerException() {
    }

    public TrackerException(String s) {
        super(s);
    }

    public TrackerException(String message, Throwable cause) {
        super(message, cause);
    }

    public TrackerException(Throwable cause) {
        super(cause);
    }
}
