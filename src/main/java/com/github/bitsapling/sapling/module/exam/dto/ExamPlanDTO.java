package com.github.bitsapling.sapling.module.exam.dto;

import com.github.bitsapling.sapling.module.exam.ExamPlan;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

import java.math.BigInteger;

@EqualsAndHashCode(callSuper = true)
@Validated
public class ExamPlanDTO extends ExamPlan {
    public ExamPlanDTO(@PositiveOrZero Long id, @NotEmpty String name, @NotNull @PositiveOrZero BigInteger duration,
                       @NotNull @PositiveOrZero BigInteger targetUploaded,
                       @NotNull @PositiveOrZero BigInteger targetDownloaded,
                       @NotNull @PositiveOrZero BigInteger targetRealUploaded,
                       @NotNull @PositiveOrZero BigInteger targetRealDownloaded,
                       @NotNull @PositiveOrZero BigInteger targetKarma,
                       @NotNull @PositiveOrZero BigInteger targetShareRatio) {
        super(id, name, duration, targetUploaded, targetDownloaded,
                targetRealUploaded, targetRealDownloaded, targetKarma, targetShareRatio);
    }
}
