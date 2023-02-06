package com.github.bitsapling.sapling.repository;

import com.github.bitsapling.sapling.entity.Torrent;
import com.github.bitsapling.sapling.entity.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TorrentRepository extends CrudRepository<Torrent, Long> {
    Optional<Torrent> findByInfoHash(@NotNull String infoHash);

    @NotNull
    @Cacheable(cacheNames = "torrent", key = "#userId")
    List<Torrent> findAllByUserId(long userId);

    @NotNull
    List<Torrent> findAllByTitle(@NotNull String title);

    @NotNull
    List<Torrent> findAllByUnderReviewIs(boolean is);

    @NotNull
    List<Torrent> findAllByAnonymousIs(boolean is);

    @NotNull
    List<Torrent> findAllByUser(@NotNull User user);
}
