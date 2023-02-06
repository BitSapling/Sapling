package com.github.bitsapling.sapling.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Exam implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private long id;
    private User user;
    private ExamPlan examPlan;

    private Timestamp endAt;
}
