package com.ghostchu.sapling.repository;

import com.ghostchu.sapling.domain.model.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @NotNull
    Optional<User> findByNickname(@NotNull String username);

    @NotNull
    List<User> findByEmail(@NotNull String email);

    @NotNull
    Optional<User> findByPasskeyEqualsIgnoreCase(@NotNull UUID passkey);

    @NotNull
    List<User> findAllByGroupId(long groupId);

    @NotNull
    List<User> findAllByInviterUid(long inviterUid);

    @NotNull
    List<User> findAllByParked(boolean parked);
}
