package com.github.bitsapling.sapling.module.promotion;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.bitsapling.sapling.module.common.CommonService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class PromotionService extends ServiceImpl<PromotionMapper, Promotion> implements CommonService<Promotion> {
    @Nullable
    public Promotion getPromotion(@NotNull Object promotion) {
        return baseMapper.selectOne(lambdaQuery().eq(Promotion::getId, promotion)
                .or(w -> w.eq(Promotion::getName, promotion)));
    }

    @Nullable
    public Promotion getDefaultPromotion() {
        return baseMapper.selectOne(lambdaQuery().eq(Promotion::getIsDefault, true));
    }
}
