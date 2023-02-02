package com.github.bitsapling.sapling.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "groups",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"id"}),

        }
)
@Data
public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private long id;
    @Column(name = "code")
    @OneToMany
    private List<Permission> permissions;

    @Column(name = "promotion_policy")
    @OneToOne
    private PromotionPolicy promotionPolicy;

    public boolean hasPermission(String permission){
        for (Permission perm : permissions) {
            if(perm.getCode().equals(permission)) {
                return true;
            }
        }
        return false;
    }
}
