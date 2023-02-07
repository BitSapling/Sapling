package com.github.bitsapling.sapling.service;

import com.github.bitsapling.sapling.entity.ExamPlan;
import com.github.bitsapling.sapling.repository.ExamPlanRepository;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ExamPlanService {
    @Autowired
    private ExamPlanRepository repository;

    @Nullable
    public ExamPlan getExamPlan(long id) {
        return repository.findById(id).orElse(null);
    }

    @Nullable
    public ExamPlan getExamPlan(@NotNull String code) {
        return repository.findByCode(code).orElse(null);
    }

}
