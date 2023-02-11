package com.github.bitsapling.sapling.repository;

import com.github.bitsapling.sapling.entity.Thanks;
import com.github.bitsapling.sapling.entity.Torrent;
import com.github.bitsapling.sapling.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThanksRepository extends CrudRepository<Thanks, Long> {
    boolean existsByTorrentAndUser(Torrent torrent, User user);

    long countAllByTorrent(Torrent torrent);

    List<Thanks> getThanksByTorrentOrderByIdDesc(Torrent torrent, Pageable pageable);
}
