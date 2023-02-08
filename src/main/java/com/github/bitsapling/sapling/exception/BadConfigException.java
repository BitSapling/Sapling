package com.github.bitsapling.sapling.exception;

public class BadConfigException extends RuntimeException {
    public BadConfigException() {
        super("This tracker have a bad configuration in the database. Please contact the administrator.");
    }
}
