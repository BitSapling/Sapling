package com.github.bitsapling.sapling.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "seedbox",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"address"})
        }
)
@Data
public class SeedBox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    @Column(name = "address", nullable = false)
    private String address;
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn(name = "download_policy")
    private PromotionPolicy downloadMultiplier;
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn(name = "download_policy")
    private PromotionPolicy uploadMultiplier;
}
