package com.github.bitsapling.sapling.entity;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Data;

@Entity
@Table(name = "seedbox",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"address"})
        }
)
@Transactional
@Data
public class SeedBoxEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    @Column(name = "address", nullable = false)
    private String address;
    @OneToOne(cascade = CascadeType.REFRESH ,fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn(name = "download_policy")
    private PromotionPolicyEntity downloadMultiplier;
    @OneToOne(cascade = CascadeType.REFRESH ,fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn(name = "download_policy")
    private PromotionPolicyEntity uploadMultiplier;
}
