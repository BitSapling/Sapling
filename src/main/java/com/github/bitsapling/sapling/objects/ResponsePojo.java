package com.github.bitsapling.sapling.objects;

import lombok.Getter;

@Getter
public abstract class ResponsePojo {
    private final long code;

    protected ResponsePojo(long code) {
        this.code = code;
    }

    public long getCode() {
        return code;
    }
}
