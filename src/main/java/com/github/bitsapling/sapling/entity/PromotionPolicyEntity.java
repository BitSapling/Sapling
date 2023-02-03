package com.github.bitsapling.sapling.entity;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Data;

@Entity
@Table(name = "promotion_policies",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"id"})
        }
)
@Transactional
@Data
public class PromotionPolicyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private long id;

    @Column(name = "display_name",nullable = false)
    private String displayName;

    @Column(name = "upload_ratio")
    private double uploadRatio;
    @Column(name = "download_ratio")
    private double downloadRatio;

    public long applyUploadRatio(double upload){
        return (long)(upload * uploadRatio);
    }
    public long applyDownloadRatio(double download){
        return (long)(download * downloadRatio);
    }
}
