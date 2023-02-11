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
import org.hibernate.annotations.CascadeType;

import java.util.List;

@Entity
@Table(name = "user_groups",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"slug"}),
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
    @Column(name = "slug", nullable = false, updatable = false)
    private String slug;
    @Column(name = "display_name", nullable = false)
    private String displayName;
    @OneToMany
    @Cascade({CascadeType.ALL})
    @PrimaryKeyJoinColumn
    @JsonManagedReference
    private List<Permission> permissionEntities;

    @PrimaryKeyJoinColumn
    @Cascade({CascadeType.ALL})
    @ManyToOne
    @JsonBackReference
    private PromotionPolicy promotionPolicy;

//    @PrimaryKeyJoinColumn
//    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
//    @OneToMany(fetch = FetchType.EAGER)
//    private List<UserGroupEntity> inherited;

    public boolean hasPermission(String permission) {
        for (Permission perm : permissionEntities) {
            if (perm.getSlug().equals(permission)) {
                return true;
            }
        }
        return false;
    }
}
