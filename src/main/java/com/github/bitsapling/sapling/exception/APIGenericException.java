package com.github.bitsapling.sapling.exception;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Immutable;
import org.springframework.http.HttpStatusCode;

import java.io.Serializable;

@Immutable
public class APIGenericException extends RuntimeException implements Serializable {
    private final APIErrorCode error;
    private final String message;

    public APIGenericException(@NotNull APIErrorCode error, @NotNull String message) {
        this.error = error;
        this.message = message;
    }
    public APIGenericException(@NotNull APIErrorCode error) {
        this.error = error;
        this.message = error.name();
    }
    public int getError() {
        return error.getCode();
    }

    public String getErrorText(){
        return error.name();
    }
    @NotNull
    public String getMessage() {
        return message;
    }

    public HttpStatusCode getStatusCode() {
        return error.getStatusCode();
    }
}
