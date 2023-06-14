package com.github.bitsapling.sapling.module.exam;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamServiceImpl extends ServiceImpl<ExamMapper, Exam> implements ExamService {

    @NotNull
    public List<Exam> getExamsByPlan(@NotNull Long examPlanId) {
        LambdaQueryWrapper<Exam> wrapper = Wrappers
                .lambdaQuery(Exam.class)
                .eq(Exam::getExamPlan, examPlanId);
        return baseMapper.selectList(wrapper);
    }

    @Nullable
    public Exam getExamByUser(@NotNull Long userId) {
        LambdaQueryWrapper<Exam> wrapper = Wrappers
                .lambdaQuery(Exam.class)
                .eq(Exam::getUser, userId);
        return baseMapper.selectOne(wrapper);
    }
}
