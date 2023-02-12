package com.github.bitsapling.sapling.service;

import com.github.bitsapling.sapling.entity.Exam;
import com.github.bitsapling.sapling.repository.ExamRepository;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Exam getExam(long id) {
        return repository.findById(id).orElse(null);
    }


}
