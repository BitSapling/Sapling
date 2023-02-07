package com.github.bitsapling.sapling.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private long id;
    @Column(name = "code", nullable = false, updatable = false)
    private String code;
    @Column(name = "display_name", nullable = false)
    private String displayName;
    @OneToMany
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    @PrimaryKeyJoinColumn
    @JsonManagedReference
    private List<Permission> permissionEntities;

    @PrimaryKeyJoinColumn
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    @ManyToOne
    @JsonBackReference
    private PromotionPolicy promotionPolicy;

//    @PrimaryKeyJoinColumn
//    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
//    @OneToMany(fetch = FetchType.EAGER)
//    private List<UserGroupEntity> inherited;

    public boolean hasPermission(String permission) {
        for (Permission perm : permissionEntities) {
            if (perm.getCode().equals(permission)) {
                return true;
            }
        }
        return false;
    }
}
