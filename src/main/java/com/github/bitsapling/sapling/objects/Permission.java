package com.github.bitsapling.sapling.objects;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@AllArgsConstructor
@Data
public class Permission implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final long id;
    private final String code;
    //private String displayName;
    private boolean def;
}
