package com.github.bitsapling.sapling.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.util.List;

@Entity
@Table(name = "user_groups",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"id"}),

        }
)
@Data

@AllArgsConstructor
@NoArgsConstructor
public class UserGroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    @Column(name = "code", nullable = false)
    private String code;
    @Column(name = "display_name", nullable = false)
    private String displayName;
    @OneToMany(fetch = FetchType.EAGER)
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    @PrimaryKeyJoinColumn
    private List<PermissionEntity> permissionEntities;

    @PrimaryKeyJoinColumn
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    @ManyToOne(fetch = FetchType.EAGER)
    private PromotionPolicyEntity promotionPolicy;

//    @PrimaryKeyJoinColumn
//    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
//    @OneToMany(fetch = FetchType.EAGER)
//    private List<UserGroupEntity> inherited;

    public boolean hasPermission(String permission) {
        for (PermissionEntity perm : permissionEntities) {
            if (perm.getCode().equals(permission)) {
                return true;
            }
        }
        return false;
    }
}
