package com.github.bitsapling.sapling.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Permission implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private  long id;
    private  String code;
    //private String displayName;
    private boolean def;
}
