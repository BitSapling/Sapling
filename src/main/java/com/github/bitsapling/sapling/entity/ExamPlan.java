package com.github.bitsapling.sapling.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Proxy;

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
@Proxy(lazy = false)
public class ExamPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    @Column(name = "code", nullable = false)
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
