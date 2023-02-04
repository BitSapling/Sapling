package com.github.bitsapling.sapling.objects;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@AllArgsConstructor
@Data
public class UserGroup {
    private final long id;
    private String displayName;
    private List<Permission> permissionEntities;
    private PromotionPolicy promotionPolicy;

}
