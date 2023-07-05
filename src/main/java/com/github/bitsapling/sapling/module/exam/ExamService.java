package com.github.bitsapling.sapling.module.exam;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.github.bitsapling.sapling.module.common.CommonService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamService extends ServiceImpl<ExamMapper, Exam> implements CommonService<Exam> {

    @NotNull
    public List<Exam> getExamsByPlan(@NotNull Long examPlanId) {
        return ChainWrappers.lambdaQueryChain(Exam.class)
                .eq(Exam::getExamPlan, examPlanId)
                .list();
    }

    @Nullable
    public Exam getExamByUser(@NotNull Long userId) {
        return ChainWrappers.lambdaQueryChain(Exam.class)
                .eq(Exam::getUser, userId)
                .one();
    }


    public boolean removeExamForUser(@NotNull Long userId) {
        return ChainWrappers.lambdaUpdateChain(Exam.class)
                .eq(Exam::getUser, userId)
                .remove();
    }
}
