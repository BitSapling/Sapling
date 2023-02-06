package com.github.bitsapling.sapling.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Proxy;

@Entity
@Table(name = "seedbox",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"address"})
        }
)

@Data
@Proxy(lazy = false)
public class SeedBox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    @Column(name = "address", nullable = false)
    private String address;
    @ManyToOne(fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    private PromotionPolicy downloadMultiplier;
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    @ManyToOne(fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
    private PromotionPolicy uploadMultiplier;
}
