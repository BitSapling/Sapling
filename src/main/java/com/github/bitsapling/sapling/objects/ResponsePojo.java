package com.github.bitsapling.sapling.objects;

import lombok.Data;
import lombok.Getter;

@Getter
public abstract class ResponsePojo {
    private final long errorCode;

    protected ResponsePojo(long errorCode) {
        this.errorCode = errorCode;
    }

    public long getErrorCode() {
        return errorCode;
    }
}
