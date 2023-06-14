package com.github.bitsapling.sapling.module.promotion;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.bitsapling.sapling.module.permission.Permission;
import com.github.bitsapling.sapling.module.permission.PermissionMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromotionService {
    @Autowired
    private PromotionMapper mapper;

    @NotNull
    public List<Promotion> getAllPromotions() {
        return mapper.selectList(Wrappers.lambdaQuery(Promotion.class));
    }

    @Nullable
    public Promotion getPromotion(@NotNull Long id) {
        return mapper.selectById(id);
    }

    @Nullable
    public Promotion getPromotion(@NotNull String name) {
        LambdaQueryWrapper<Promotion> wrapper = Wrappers
                .lambdaQuery(Promotion.class)
                .eq(Promotion::getName, name);
        return mapper.selectOne(wrapper);
    }

    @Nullable
    public Promotion getDefaultPromotion() {
        LambdaQueryWrapper<Promotion> wrapper = Wrappers
                .lambdaQuery(Promotion.class)
                .eq(Promotion::getIsDefault, true);
        return mapper.selectOne(wrapper);
    }

    public int addPromotion(@NotNull Promotion promotion) {
        return mapper.insert(promotion);
    }

    public int deletePromotion(@NotNull Promotion promotion) {
        return mapper.deleteById(promotion.getId());
    }

    public int deletePromotion(@NotNull Long id) {
        return mapper.deleteById(id);
    }

    public int updatePromotion(@NotNull Promotion promotion) {
        return mapper.updateById(promotion);
    }
}
