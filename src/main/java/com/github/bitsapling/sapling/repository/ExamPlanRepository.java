package com.github.bitsapling.sapling.repository;

import com.github.bitsapling.sapling.entity.ExamPlan;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ExamPlanRepository extends CrudRepository<ExamPlan, Long> {
    Optional<ExamPlan> findByCode(String code);

    Optional<ExamPlan> findByDisplayName(String displayName);

    @Override
    @NotNull
    Optional<ExamPlan> findById(@NotNull Long id);

}
