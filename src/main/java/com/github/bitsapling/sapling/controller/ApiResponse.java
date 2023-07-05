package com.github.bitsapling.sapling.controller;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Data
public class ApiResponse<T> {
    private HttpStatusCode httpCode;
    private String description;
    private LocalDateTime time;
    private T data;

    public ApiResponse(HttpStatus httpCode, String description, LocalDateTime time, T data) {
        this.httpCode = httpCode;
        this.description = description;
        this.time = time;
        this.data = data;
    }

    public ApiResponse(HttpStatus httpCode, String description, T data) {
        this.httpCode = httpCode;
        this.description = description;
        this.time = LocalDateTime.now();
        this.data = data;
    }

    public ApiResponse(HttpStatus httpCode, String description) {
        this.httpCode = httpCode;
        this.description = description;
        this.time = LocalDateTime.now();
        this.data = null;
    }

    public ApiResponse(T data) {
        this.httpCode = HttpStatus.OK;
        this.description = "ok";
        this.time = LocalDateTime.now();
        this.data = data;
    }


    @NotNull
    public static ApiResponse<Void> ok() {
        return new ApiResponse<>(HttpStatus.OK, "ok", LocalDateTime.now(), null);
    }

    @NotNull
    public static ApiResponse<Void> notFound() {
        return new ApiResponse<>(HttpStatus.NOT_FOUND, "resource not found", LocalDateTime.now(), null);
    }

    @NotNull
    public static ApiResponse<Void> internalError() {
        return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "internal server error (undefined)", LocalDateTime.now(), null);
    }

    @NotNull
    public static ApiResponse<Void> noContent() {
        return new ApiResponse<>(HttpStatus.NO_CONTENT, "no content", LocalDateTime.now(), null);
    }

    @NotNull
    public static ApiResponse<Void> forbidden() {
        return new ApiResponse<>(HttpStatus.FORBIDDEN, "access denied", LocalDateTime.now(), null);
    }


}
