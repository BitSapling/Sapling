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
@Table(name = "exam_plan",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"id"}),
                @UniqueConstraint(columnNames = {"code"})
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private long id;
    @Column(name = "code", nullable = false, updatable = false)
    private String code;
    @Column(name = "displayName", nullable = false)
    private String displayName;
    @Column(name = "uploaded", nullable = false)
    private long uploaded;
    @Column(name = "downloaded", nullable = false)
    private long downloaded;
    @Column(name = "karma", nullable = false)
    private double karma;
    @Column(name = "seeds", nullable = false)
    private long seeds;
    @Column(name = "seedingTime", nullable = false)
    private long seedingTime;
    @Column(name = "shareRatio", nullable = false)
    private double shareRatio;
    @Column(name = "duration", nullable = false)
    private long duration;
}
