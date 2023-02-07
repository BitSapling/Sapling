package com.github.bitsapling.sapling.repository;

import com.github.bitsapling.sapling.entity.Tag;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {
    Optional<Tag> findByName(@NotNull String name);
}
