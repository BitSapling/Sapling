package com.github.bitsapling.sapling.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.sql.Timestamp;

@Entity
@Table(name = "exams",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id"})
        },
        indexes = {
                @Index(columnList = "user_id")
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private long id;
    @PrimaryKeyJoinColumn
    @Cascade({CascadeType.ALL})
    @ManyToOne
    @JsonBackReference
    private ExamPlan examPlan;
    @PrimaryKeyJoinColumn
    @Cascade({CascadeType.ALL})
    @ManyToOne
    @JsonBackReference
    private User user;
    @Column(name = "end_at", nullable = false)
    private Timestamp endAt;
}
