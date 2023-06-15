package com.github.bitsapling.sapling.module.exam;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.bitsapling.sapling.module.common.CommonService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class ExamPlanService extends ServiceImpl<ExamPlanMapper, ExamPlan> implements CommonService<ExamPlan> {
    @Nullable
    public ExamPlan getExamPlanByName(@NotNull String name) {
        LambdaQueryWrapper<ExamPlan> wrapper = Wrappers
                .lambdaQuery(ExamPlan.class)
                .eq(ExamPlan::getName, name);
        return baseMapper.selectOne(wrapper);
    }
}
