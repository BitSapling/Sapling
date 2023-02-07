package com.github.bitsapling.sapling.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

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
    @Column(name = "id", nullable = false, updatable = false)
    private long id;
    @Column(name = "address", nullable = false)
    private String address;
    @ManyToOne
    @PrimaryKeyJoinColumn
    @Cascade({CascadeType.ALL})
    @JsonBackReference
    private PromotionPolicy downloadMultiplier;
    @Cascade({CascadeType.ALL})
    @ManyToOne
    @PrimaryKeyJoinColumn
    @JsonBackReference
    private PromotionPolicy uploadMultiplier;
}
