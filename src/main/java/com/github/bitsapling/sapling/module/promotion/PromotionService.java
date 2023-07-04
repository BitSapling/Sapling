package com.github.bitsapling.sapling.module.promotion;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.github.bitsapling.sapling.module.common.CommonService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class PromotionService extends ServiceImpl<PromotionMapper, Promotion> implements CommonService<Promotion> {
    @Nullable
    public Promotion getPromotion(@NotNull Object promotion) {
        return ChainWrappers.lambdaQueryChain(Promotion.class)
                .eq(Promotion::getId, promotion)
                .or(w -> w.eq(Promotion::getName, promotion))
                .one();
    }

    @Nullable
    public Promotion getDefaultPromotion() {
        return ChainWrappers.lambdaQueryChain(Promotion.class)
                .eq(Promotion::getIsDefault, true)
                .one();
    }
}
