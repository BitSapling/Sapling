package com.github.bitsapling.sapling.entity;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "promotion_policies",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"id"})
        }
)

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionPolicyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(name = "upload_ratio")
    private double uploadRatio;
    @Column(name = "download_ratio")
    private double downloadRatio;
}
