package com.github.bitsapling.sapling.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
public class PromotionPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private long id;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(name = "upload_ratio")
    private double uploadRatio;
    @Column(name = "download_ratio")
    private double downloadRatio;

    public double applyUploadRatio(double upload) {
        return upload * uploadRatio;
    }
    public double applyDownloadRatio(double download) {
        return download * downloadRatio;
    }
}
