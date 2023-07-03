package com.github.bitsapling.sapling.controller;

import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiResponse<T> {
    private Integer code;
    private String description;
    private LocalDateTime time;
    private T data;

    public ApiResponse(Integer code, String description, LocalDateTime time, T data) {
        this.code = code;
        this.description = description;
        this.time = time;
        this.data = data;
    }

    public ApiResponse(Integer code, String description, T data) {
        this.code = code;
        this.description = description;
        this.time = LocalDateTime.now();
        this.data = data;
    }

    public ApiResponse(Integer code, String description) {
        this.code = code;
        this.description = description;
        this.time = LocalDateTime.now();
        this.data = null;
    }

    public ApiResponse(T data) {
        this.code = 0;
        this.description = "success";
        this.time = LocalDateTime.now();
        this.data = data;
    }


    @NotNull
    public static ApiResponse<Void> ok() {
        return new ApiResponse<>(0, "success", LocalDateTime.now(), null);
    }

    @NotNull
    public static ApiResponse<Void> notFound() {
        return new ApiResponse<>(404, "resource not found", LocalDateTime.now(), null);
    }

    @NotNull
    public static ApiResponse<Void> internalError() {
        return new ApiResponse<>(500, "internal server error (undefined)", LocalDateTime.now(), null);
    }

    @NotNull
    public static ApiResponse<Void> noContent() {
        return new ApiResponse<>(204, "no content", LocalDateTime.now(), null);
    }
}
