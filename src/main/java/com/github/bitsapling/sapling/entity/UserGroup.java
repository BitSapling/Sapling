package com.github.bitsapling.sapling.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "user_groups",
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
    @Column(name = "display_name", nullable = false)
    private String displayName;
    @OneToMany(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn(name="permissions")
    private List<Permission> permissions;

    @PrimaryKeyJoinColumn(name = "promotion_policy")
    @OneToOne(cascade = CascadeType.ALL)
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
