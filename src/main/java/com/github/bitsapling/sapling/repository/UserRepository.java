package com.github.bitsapling.sapling.repository;

import com.github.bitsapling.sapling.entity.User;
import com.github.bitsapling.sapling.entity.UserGroup;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(@NotNull String username);

    Optional<User> findByEmail(@NotNull String email);

    Optional<User> findByPasskeyIgnoreCase(@NotNull String passkey);

    Optional<User> findByPersonalAccessTokenIgnoreCase(@NotNull String personalAccessToken);

    List<User> findByEmailContains(@NotNull String emailPart);

    List<User> findByUsernameContains(@NotNull String usernamePart);

    List<User> findByGroup(@NotNull UserGroup group);
}
