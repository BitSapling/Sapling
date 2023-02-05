package com.github.bitsapling.sapling.repository;

import com.github.bitsapling.sapling.entity.TorrentEntity;
import com.github.bitsapling.sapling.entity.UserEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TorrentRepository extends CrudRepository<TorrentEntity, Long> {
    Optional<TorrentEntity> findByInfoHash(@NotNull String infoHash);

    @NotNull
    @Cacheable(cacheNames = "torrent", key = "#userId")
    List<TorrentEntity> findAllByUserId(long userId);

    @NotNull
    List<TorrentEntity> findAllByTitle(@NotNull String title);

    @NotNull
    List<TorrentEntity> findAllByUnderReviewIs(boolean is);

    @NotNull
    List<TorrentEntity> findAllByAnonymousIs(boolean is);

    @NotNull
    List<TorrentEntity> findAllByUser(@NotNull UserEntity user);
}
