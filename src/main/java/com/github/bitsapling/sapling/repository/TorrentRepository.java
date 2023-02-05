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
    @Cacheable(cacheNames = "torrent", key = "'info_hash='.concat(#infoHash)")
    Optional<TorrentEntity> findByInfoHash(@NotNull String infoHash);

    @NotNull
    @Cacheable(cacheNames = "torrent", key = "#userId")
    List<TorrentEntity> findAllByUserId(long userId);

    @NotNull
    @Cacheable(cacheNames = "torrent", key = "'title='.concat(#title)")
    List<TorrentEntity> findAllByTitle(@NotNull String title);

    @NotNull
    @Cacheable(cacheNames = "torrent", key = "'under_review='.concat(#is)")
    List<TorrentEntity> findAllByUnderReviewIs(boolean is);

    @NotNull
    @Cacheable(cacheNames = "torrent", key = "'anonymous='.concat(#is)")
    List<TorrentEntity> findAllByAnonymousIs(boolean is);

    @NotNull
    @Cacheable(cacheNames = "torrent", key = "'user='.concat(#user.id)")
    List<TorrentEntity> findAllByUser(@NotNull UserEntity user);
}
