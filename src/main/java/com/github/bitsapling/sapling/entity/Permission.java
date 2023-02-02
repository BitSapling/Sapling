package com.github.bitsapling.sapling.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "permissions",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"id"}),
                @UniqueConstraint(columnNames = {"code"})
        }
)
@Data
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private long id;
    @Column(name="code",nullable = false)
    private String code;
    @Column(name="display_name",nullable = false)
    private String displayName;
}
