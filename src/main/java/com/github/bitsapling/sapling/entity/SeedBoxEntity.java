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
    @ManyToOne(cascade = CascadeType.MERGE ,fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
    private PromotionPolicyEntity downloadMultiplier;
    @ManyToOne(cascade = CascadeType.MERGE ,fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
    private PromotionPolicyEntity uploadMultiplier;
}
