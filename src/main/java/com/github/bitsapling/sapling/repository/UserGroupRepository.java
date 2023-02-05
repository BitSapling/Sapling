package com.github.bitsapling.sapling.repository;

import com.github.bitsapling.sapling.entity.UserGroupEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserGroupRepository extends CrudRepository<UserGroupEntity, Long> {
    @Override
    @NotNull
    Optional<UserGroupEntity> findById(@NotNull Long aLong);
}
