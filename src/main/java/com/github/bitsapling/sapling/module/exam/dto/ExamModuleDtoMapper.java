package com.github.bitsapling.sapling.module.exam.dto;

import com.github.bitsapling.sapling.module.exam.Exam;
import com.github.bitsapling.sapling.module.exam.ExamPlan;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ExamModuleDtoMapper {
    ExamModuleDtoMapper INSTANCE = Mappers.getMapper(ExamModuleDtoMapper.class);

    DeployedExam toDeployedExam(Exam exam);

    PublishedExamPlan toPublishedExamPlan(ExamPlan examPlan);


}
