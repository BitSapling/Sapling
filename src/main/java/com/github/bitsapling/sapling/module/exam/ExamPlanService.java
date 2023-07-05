package com.github.bitsapling.sapling.module.exam;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.github.bitsapling.sapling.module.common.CommonService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class ExamPlanService extends ServiceImpl<ExamPlanMapper, ExamPlan> implements CommonService<ExamPlan> {
    @Nullable
    public ExamPlan getExamPlan(@NotNull Object identifier) {
        return ChainWrappers.lambdaQueryChain(ExamPlan.class)
                .eq(ExamPlan::getId, identifier)
                .or(w -> w.eq(ExamPlan::getName, identifier))
                .one();
    }
}
