package com.github.bitsapling.sapling.entity;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "user_groups",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"id"}),

        }
)
@Data
@Transactional
public class UserGroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private long id;
    @Column(name = "display_name", nullable = false)
    private String displayName;
    @OneToMany(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn(name="permissions")
    private List<PermissionEntity> permissionEntities;

    @PrimaryKeyJoinColumn(name = "promotion_policy")
    @OneToOne(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
    private PromotionPolicyEntity promotionPolicy;

    public boolean hasPermission(String permission){
        for (PermissionEntity perm : permissionEntities) {
            if(perm.getCode().equals(permission)) {
                return true;
            }
        }
        return false;
    }
}
