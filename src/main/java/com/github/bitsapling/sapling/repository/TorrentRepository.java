package com.github.bitsapling.sapling.repository;

import com.github.bitsapling.sapling.entity.Category;
import com.github.bitsapling.sapling.entity.Torrent;
import com.github.bitsapling.sapling.entity.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TorrentRepository extends JpaRepository<Torrent, Long>, JpaSpecificationExecutor<Torrent>{
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

    @NotNull
    List<Torrent> findAllByCategory(@NotNull Category category);

    Page<Torrent> searchByTitleLikeIgnoreCase(@NotNull String keyword, @NotNull Pageable pageable);
}
