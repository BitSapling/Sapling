package com.github.bitsapling.sapling.module.category;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Nullable
    public Category getCategory(@NotNull String name) {
        LambdaQueryWrapper<Category> wrapper = Wrappers
                .lambdaQuery(Category.class)
                .eq(Category::getName, name);
        return baseMapper.selectOne(wrapper);
    }
}
