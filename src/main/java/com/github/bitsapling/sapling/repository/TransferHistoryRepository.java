package com.github.bitsapling.sapling.repository;

import com.github.bitsapling.sapling.entity.Torrent;
import com.github.bitsapling.sapling.entity.TransferHistory;
import com.github.bitsapling.sapling.entity.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface TransferHistoryRepository extends CrudRepository<TransferHistory, Long> {
    Optional<TransferHistory> findByUserAndTorrent(@NotNull User user, @NotNull Torrent torrent);

    List<TransferHistory> findAllByUserOrderByUpdatedAt(@NotNull User user);

    List<TransferHistory> findAllByTorrentOrderByUpdatedAt(@NotNull Torrent torrent);
}
