package com.github.bitsapling.sapling.objects;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@AllArgsConstructor
@Data
public class Exam {
    private long id;
    private User user;
    private ExamPlan examPlan;
    private Instant endAt;
}
