package com.github.bitsapling.sapling.objects;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
@AllArgsConstructor
@Data
public class ExamPlan implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private long id;
    private String code;
    private String displayName;
    private long uploaded;
    private long downloaded;
    private double karma;
    private long seeds;
    private long seedingTime;
    private double shareRatio;
    private long duration;
}
