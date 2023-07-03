package com.github.bitsapling.sapling.module.category;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.bitsapling.sapling.module.common.CommonService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class CategoryService extends ServiceImpl<CategoryMapper, Category> implements CommonService<Category> {
    @Nullable
    public Category getCategory(@NotNull Object identifier) {
        return baseMapper.selectOne(lambdaQuery()
                .eq(Category::getId, identifier)
                .or(w -> w.eq(Category::getName, identifier)));
    }


}
