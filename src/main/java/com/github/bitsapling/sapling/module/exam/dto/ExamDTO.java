package com.github.bitsapling.sapling.module.exam.dto;

import com.github.bitsapling.sapling.module.exam.Exam;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Validated
public class ExamDTO extends Exam {

    public ExamDTO(@PositiveOrZero Long id, @Positive Long user, @Positive Long examPlan, LocalDateTime startedAt, @Future LocalDateTime endAt, @PositiveOrZero Short status) {
        super(id, user, examPlan, startedAt, endAt, status);
    }
}
