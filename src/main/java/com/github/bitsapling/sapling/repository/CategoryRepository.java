package com.github.bitsapling.sapling.repository;

import com.github.bitsapling.sapling.entity.Category;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {
    Optional<Category> findBySlug(@NotNull String slug);
}
