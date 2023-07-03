package com.github.bitsapling.sapling.module.exam;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.bitsapling.sapling.module.common.CommonService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamService extends ServiceImpl<ExamMapper, Exam> implements CommonService<Exam> {

    @NotNull
    public List<Exam> getExamsByPlan(@NotNull Long examPlanId) {
        return baseMapper.selectList(lambdaQuery().eq(Exam::getExamPlan, examPlanId));
    }

    @Nullable
    public Exam getExamByUser(@NotNull Long userId) {
        return baseMapper.selectOne(lambdaQuery().eq(Exam::getUser, userId));
    }


    public int removeExamForUser(@NotNull Long userId) {
        return baseMapper.delete(lambdaQuery().eq(Exam::getUser, userId));
    }
}
