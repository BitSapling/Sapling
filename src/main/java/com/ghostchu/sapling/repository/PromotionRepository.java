package com.ghostchu.sapling.repository;

import com.ghostchu.sapling.domain.model.Promotion;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    @NotNull
    Optional<Promotion> findByName(@NotNull String name);
}
