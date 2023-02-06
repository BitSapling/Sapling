package com.github.bitsapling.sapling.repository;

import com.github.bitsapling.sapling.entity.UserGroup;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserGroupRepository extends CrudRepository<UserGroup, Long> {
    @Override
    @NotNull
    Optional<UserGroup> findById(@NotNull Long aLong);
}
