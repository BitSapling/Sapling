package com.github.bitsapling.sapling.entity;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.List;

@Entity
@Table(name = "user_groups",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"id"}),

        }
)
@Data
@Transactional
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    @Column(name = "display_name", nullable = false)
    private String displayName;
    @OneToMany(fetch = FetchType.EAGER)
    @Cascade(CascadeType.SAVE_UPDATE)
    @PrimaryKeyJoinColumn
    private List<PermissionEntity> permissionEntities;

    @PrimaryKeyJoinColumn
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @ManyToOne(fetch = FetchType.EAGER)
    private PromotionPolicyEntity promotionPolicy;

    public boolean hasPermission(String permission) {
        for (PermissionEntity perm : permissionEntities) {
            if (perm.getCode().equals(permission)) {
                return true;
            }
        }
        return false;
    }
}
