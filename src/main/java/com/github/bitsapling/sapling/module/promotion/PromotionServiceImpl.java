package com.github.bitsapling.sapling.module.promotion;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class PromotionServiceImpl extends ServiceImpl<PromotionMapper, Promotion> implements PromotionService {
    @Nullable
    public Promotion getPromotion(@NotNull String name) {
        LambdaQueryWrapper<Promotion> wrapper = Wrappers
                .lambdaQuery(Promotion.class)
                .eq(Promotion::getName, name);
        return baseMapper.selectOne(wrapper);
    }

    @Nullable
    public Promotion getDefaultPromotion() {
        LambdaQueryWrapper<Promotion> wrapper = Wrappers
                .lambdaQuery(Promotion.class)
                .eq(Promotion::getIsDefault, true);
        return baseMapper.selectOne(wrapper);
    }
}
