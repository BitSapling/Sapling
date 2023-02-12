package com.github.bitsapling.sapling.service;

import com.github.bitsapling.sapling.entity.Category;
import com.github.bitsapling.sapling.repository.CategoryRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service

public class CategoryService {
    @Autowired
    private CategoryRepository repository;

    @Nullable
    public Category getCategory(@NotNull String slug) {
        return repository.findBySlug(slug).orElse(null);
    }

    @Nullable
    public Category getCategory(long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        repository.findAll().forEach(categories::add);
        return categories;
    }

    @NotNull
    public Category save(@NotNull Category category) {
        return repository.save(category);
    }
}
