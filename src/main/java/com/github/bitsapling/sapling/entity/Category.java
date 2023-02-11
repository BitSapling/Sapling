package com.github.bitsapling.sapling.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "categories",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"slug"})
        },
        indexes = {
                @Index(columnList = "slug")
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private long id;
    @Column(name = "slug", nullable = false, updatable = false)
    @Cascade({CascadeType.ALL})
    private String slug;
    @Column(name = "name", nullable = false)
    @Cascade({CascadeType.ALL})
    private String name;
    @Column(name = "icon", nullable = false)
    private String icon;
}
