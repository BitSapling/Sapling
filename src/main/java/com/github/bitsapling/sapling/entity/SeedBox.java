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
    @Column(name = "download_multipler")
    private Double downloadMultiplier;
    @Column(name = "upload_multipler")
    private Double uploadMultiplier;
}
