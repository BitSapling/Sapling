package com.github.bitsapling.sapling.objects;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
@AllArgsConstructor
@Data
public class UserGroup  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final long id;
    private String code;
    private String displayName;
    private List<Permission> permissionEntities;
    private PromotionPolicy promotionPolicy;
   // private List<UserGroup> inherited;

}
