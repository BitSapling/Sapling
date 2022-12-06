package com.ghostchu.sapling.exception;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InvalidTorrentVerifyException extends TorrentException {
    private final String field;
    private final String exceptedType;
    private final String actualType;

    public InvalidTorrentVerifyException(@NotNull String field, @NotNull Class<?> excepted, @Nullable Object actual) {
        super("The field " + field + " are unexpected. Excepted type: " + excepted.getName() + ", Actual type: " + (actual == null ? "null" : actual.getClass().getName()));
        this.field = field;
        this.exceptedType = excepted.getName();
        this.actualType = actual == null ? "null" : actual.getClass().getName();

    }

    @NotNull
    public String getActualType() {
        return actualType;
    }

    @NotNull
    public String getExceptedType() {
        return exceptedType;
    }

    @NotNull
    public String getField() {
        return field;
    }
}
