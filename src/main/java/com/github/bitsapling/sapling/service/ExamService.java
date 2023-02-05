package com.github.bitsapling.sapling.service;

import com.github.bitsapling.sapling.entity.ExamEntity;
import com.github.bitsapling.sapling.objects.Exam;
import com.github.bitsapling.sapling.repository.ExamRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ExamService {
    @Autowired
    private ExamRepository repository;
    @Autowired
    private UserService userService;
    @Autowired
    private ExamPlanService examPlanService;

    @Nullable
    @Cacheable(cacheNames = "exam", key = "id-#id")
    public Exam getExam(long id) {
        return repository.findById(id).map(this::convert).orElse(null);
    }

    @NotNull
    public Exam convert(@NotNull ExamEntity entity) {
        return new Exam(
                entity.getId(),
                userService.convert(entity.getUser()),
                examPlanService.convert(entity.getExamPlan()),
                entity.getEndAt()
        );
    }

    @NotNull
    public ExamEntity convert(@NotNull Exam exam) {
        return new ExamEntity(
                exam.getId(),
                examPlanService.convert(exam.getExamPlan()),
                userService.convert(exam.getUser()),
                exam.getEndAt()
        );
    }
}
