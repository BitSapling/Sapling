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
    INVALID_CATEGORY(9, HttpStatus.NOT_FOUND),
    EMAIL_ALREADY_IN_USAGE(10, HttpStatus.CONFLICT),
    USERNAME_ALREADY_IN_USAGE(11, HttpStatus.CONFLICT),
    TOO_MANY_FAILED_AUTHENTICATION_ATTEMPTS(12, HttpStatus.TOO_MANY_REQUESTS),
    MAX_UPLOAD_SIZE_EXCEEDED(13, HttpStatus.PAYLOAD_TOO_LARGE),
    YOU_ALREADY_THANKED_THIS_TORRENT(14, HttpStatus.NOT_MODIFIED);

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
