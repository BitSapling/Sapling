package com.github.bitsapling.sapling.repository;

import com.github.bitsapling.sapling.entity.Torrent;
import com.github.bitsapling.sapling.entity.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TorrentRepository extends CrudRepository<Torrent, Long> {
    Optional<Torrent> findByInfoHashIgnoreCase(@NotNull String infoHash);

    @NotNull
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
