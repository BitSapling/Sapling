package com.github.bitsapling.sapling.repository;

import com.github.bitsapling.sapling.entity.PermissionEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends CrudRepository<PermissionEntity, Long> {
    Optional<PermissionEntity> findByCode(@NotNull String code);
}
