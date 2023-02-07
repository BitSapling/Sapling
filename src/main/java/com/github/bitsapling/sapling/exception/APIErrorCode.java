package com.github.bitsapling.sapling.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum APIErrorCode {
    MISSING_PARAMETERS(1, HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(2, HttpStatus.NOT_FOUND),
    AUTHENTICATION_FAILED(3, HttpStatus.FORBIDDEN),
    REQUIRED_AUTHENTICATION(4, HttpStatus.UNAUTHORIZED),
    INVALID_TORRENT_FILE(5, HttpStatus.NOT_ACCEPTABLE),
    TORRENT_ALREADY_EXISTS(6, HttpStatus.CONFLICT),
    TORRENT_NOT_EXISTS(7, HttpStatus.NOT_FOUND),
    TORRENT_FILE_MISSING(8, HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_CATEGORY(9, HttpStatus.NOT_FOUND);

    private final int code;
    private final HttpStatusCode statusCode;

    APIErrorCode(int code, HttpStatus statusCode) {
        this.code = code;
        this.statusCode = statusCode;
    }

    APIErrorCode(int code, HttpStatusCode statusCode) {
        this.code = code;
        this.statusCode = statusCode;
    }

    public int getCode() {
        return code;
    }


    public HttpStatusCode getStatusCode() {
        return statusCode;
    }
}
