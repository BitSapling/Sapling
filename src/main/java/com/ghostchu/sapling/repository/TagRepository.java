package com.ghostchu.sapling.repository;

import com.ghostchu.sapling.domain.model.Tag;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    @NotNull
    @Query(value = "SELECT * FROM `tags`", nativeQuery = true)
    List<Tag> listAllTags();

    @NotNull
    Optional<Tag> findByNameEqualsIgnoreCase(@NotNull String name);

    @NotNull
    List<Tag> findByNameContainsIgnoreCase(@NotNull String name);

}
