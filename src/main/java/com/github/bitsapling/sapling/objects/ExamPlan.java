package com.github.bitsapling.sapling.objects;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;
@AllArgsConstructor
@Data
public class ExamPlan {
    private long id;
    private String code;
    private String displayName;
    private long uploaded;
    private long downloaded;
    private double karma;
    private long seeds;
    private long seedingTime;
    private double shareRatio;
    private Duration duration;
}
