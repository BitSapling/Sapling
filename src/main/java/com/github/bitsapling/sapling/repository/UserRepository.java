package com.github.bitsapling.sapling.repository;

import com.github.bitsapling.sapling.entity.UserEntity;
import com.github.bitsapling.sapling.entity.UserGroupEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(@NotNull String username);

    Optional<UserEntity> findByEmail(@NotNull String email);

    @Cacheable(cacheNames = "user", key = "'passkey='.concat(#passkey)")
    Optional<UserEntity> findByPasskey(@NotNull String passkey);

    List<UserEntity> findByEmailContains(@NotNull String emailPart);

    List<UserEntity> findByUsernameContains(@NotNull String usernamePart);

    List<UserEntity> findByGroup(@NotNull UserGroupEntity group);
}
