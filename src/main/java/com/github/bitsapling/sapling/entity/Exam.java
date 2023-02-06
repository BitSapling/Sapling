package com.github.bitsapling.sapling.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.sql.Timestamp;

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

public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    @PrimaryKeyJoinColumn
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private ExamPlan examPlan;
    @PrimaryKeyJoinColumn
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User user;
    @Column(name = "end_at", nullable = false)
    private Timestamp endAt;
}
