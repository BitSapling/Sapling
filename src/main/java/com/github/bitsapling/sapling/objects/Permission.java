package com.github.bitsapling.sapling.objects;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Permission {
    private final long id;
    private final String code;
    //private String displayName;
    private boolean def;
}
