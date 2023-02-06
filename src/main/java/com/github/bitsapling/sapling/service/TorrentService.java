package com.github.bitsapling.sapling.service;

import com.github.bitsapling.sapling.entity.Torrent;
import com.github.bitsapling.sapling.repository.TorrentRepository;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class TorrentService {
    @Autowired
    private TorrentRepository torrentRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PromotionService promotionService;

    @Nullable
    public Torrent getTorrent(long id) {
        return torrentRepository.findById(id).orElse(null);
    }

    @Nullable
    @Cacheable(cacheNames ={"torrent"}, key = "#infoHash")
    public Torrent getTorrent(@NotNull String infoHash) {
        Optional<Torrent> entity = torrentRepository.findByInfoHash(infoHash);
        return entity.orElse(null);
    }

    public Torrent save(@NotNull Torrent torrent) {
       return torrentRepository.save(torrent);
    }

}
