package com.github.bitsapling.sapling.plugin.java;

public class InvalidPluginException extends Exception {
    public InvalidPluginException() {
        super();
    }

    public InvalidPluginException(String message) {
        super(message);
    }

    public InvalidPluginException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPluginException(Throwable cause) {
        super(cause);
    }

    public InvalidPluginException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
