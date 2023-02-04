package com.github.bitsapling.sapling.service;

import com.github.bitsapling.sapling.entity.ExamPlanEntity;
import com.github.bitsapling.sapling.objects.ExamPlan;
import com.github.bitsapling.sapling.repository.ExamPlanRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ExamPlanService {
    @Autowired
    private ExamPlanRepository repository;

    @Nullable
    @Cacheable(cacheNames = "exam_plan", key = "#id")
    public ExamPlan getExamPlan(long id) {
        return repository.findById(id).map(this::convert).orElse(null);
    }

    @Nullable
    @Cacheable(cacheNames = "exam_plan", key = "#code")
    public ExamPlan getExamPlan(@NotNull String code) {
        return repository.findByCode(code).map(this::convert).orElse(null);
    }

    @NotNull
    public ExamPlan convert(@NotNull ExamPlanEntity entity) {
        return new ExamPlan(
                entity.getId(),
                entity.getCode(),
                entity.getDisplayName(),
                entity.getUploaded(),
                entity.getDownloaded(),
                entity.getKarma(),
                entity.getSeeds(),
                entity.getSeedingTime(),
                entity.getShareRatio(),
                entity.getDuration()
        );
    }

    @NotNull
    public ExamPlanEntity convert(@NotNull ExamPlan examPlan) {
        return new ExamPlanEntity(
                examPlan.getId(),
                examPlan.getCode(),
                examPlan.getDisplayName(),
                examPlan.getUploaded(),
                examPlan.getDownloaded(),
                examPlan.getKarma(),
                examPlan.getSeeds(),
                examPlan.getSeedingTime(),
                examPlan.getShareRatio(),
                examPlan.getDuration()
        );
    }
}
