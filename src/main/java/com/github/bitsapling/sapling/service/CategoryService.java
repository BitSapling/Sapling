package com.github.bitsapling.sapling.service;

import com.github.bitsapling.sapling.entity.Category;
import com.github.bitsapling.sapling.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CategoryService {
    @Autowired
    private CategoryRepository repository;

    @Nullable
    public Category getCategory(@NotNull String slug) {
        return repository.findBySlug(slug).orElse(null);
    }
    @Nullable
    public Category getCategory(long id){
        return repository.findById(id).orElse(null);
    }

    @NotNull
    public Category save(@NotNull Category category){
        return repository.save(category);
    }
}
