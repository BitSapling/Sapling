package com.github.bitsapling.sapling.entity;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.time.Instant;

@Entity
@Table(name = "exam",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"id"})
                , @UniqueConstraint(columnNames = {"user_id"})
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor

public class ExamEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    @PrimaryKeyJoinColumn
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private ExamPlanEntity examPlan;
    @PrimaryKeyJoinColumn
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private UserEntity user;
    @Column(name = "end_at", nullable = false)
    private Instant endAt;
}
