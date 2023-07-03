package com.github.bitsapling.sapling.controller;

public enum ApiCode {
    OK(0),
    AUTHENTICATION_FAILED(1),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),

    NOT_FOUND(404);
    private int code;

    ApiCode(int code) {
        this.code = code;
    }

    public static ApiCode fromCode(int code) {
        for (ApiCode apiCode : ApiCode.values()) {
            if (apiCode.getCode() == code) {
                return apiCode;
            }
        }
        return null;
    }

    public int code() {
        return code;
    }

    public int getCode() {
        return code;
    }
}
