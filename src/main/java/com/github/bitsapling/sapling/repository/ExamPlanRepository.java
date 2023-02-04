package com.github.bitsapling.sapling.repository;

import com.github.bitsapling.sapling.entity.ExamPlanEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ExamPlanRepository extends CrudRepository<ExamPlanEntity, Long> {
    Optional<ExamPlanEntity> findByCode(String code);
    Optional<ExamPlanEntity> findByDisplayName(String displayName);
}
