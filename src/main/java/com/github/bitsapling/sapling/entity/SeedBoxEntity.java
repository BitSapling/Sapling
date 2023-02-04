package com.github.bitsapling.sapling.entity;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

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
    @ManyToOne(fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
    @Cascade(CascadeType.SAVE_UPDATE)
    private PromotionPolicyEntity downloadMultiplier;
    @Cascade(CascadeType.SAVE_UPDATE)
    @ManyToOne(fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
    private PromotionPolicyEntity uploadMultiplier;
}
