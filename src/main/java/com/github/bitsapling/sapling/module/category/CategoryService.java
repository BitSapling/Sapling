package com.github.bitsapling.sapling.module.category;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryMapper mapper;

    @NotNull
    public List<Category> getCategories() {
        return mapper.selectList(Wrappers.lambdaQuery(Category.class));
    }

    @Nullable
    public Category getCategory(@NotNull Long id) {
        return mapper.selectById(id);
    }

    @Nullable
    public Category getCategory(@NotNull String name) {
        LambdaQueryWrapper<Category> wrapper = Wrappers
                .lambdaQuery(Category.class)
                .eq(Category::getName, name);
        return mapper.selectOne(wrapper);
    }

    public int addCategory(@NotNull Category category) {
        return mapper.insert(category);
    }

    public int deleteCategory(@NotNull Category category) {
        return mapper.deleteById(category.getId());
    }

    public int deleteCategory(@NotNull Long id) {
        return mapper.deleteById(id);
    }

    public int updateCategory(@NotNull Category category) {
        return mapper.updateById(category);
    }

}
