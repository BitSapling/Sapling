package com.github.bitsapling.sapling.service;

import com.github.bitsapling.sapling.entity.Thanks;
import com.github.bitsapling.sapling.entity.Torrent;
import com.github.bitsapling.sapling.entity.User;
import com.github.bitsapling.sapling.repository.ThanksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class ThanksService {
    @Autowired
    private ThanksRepository repository;

    public boolean sayThanks(Torrent torrent, User user) {
        if (repository.existsByTorrentAndUser(torrent, user)) {
            return false;
        }
        repository.save(new Thanks(0, user, torrent));
        return true;
    }

    public boolean hadThanksFor(Torrent torrent, User user) {
        return repository.existsByTorrentAndUser(torrent, user);
    }

    public List<Thanks> getLast25ThanksByTorrent(Torrent torrent) {
        return repository.getThanksByTorrentOrderByIdDesc(torrent, Pageable.ofSize(25).withPage(0));
    }

    public long countThanksForTorrent(Torrent torrent) {
        return repository.countAllByTorrent(torrent);
    }

}
