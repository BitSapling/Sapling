package com.github.bitsapling.sapling.service;

import com.github.bitsapling.sapling.entity.Category;
import com.github.bitsapling.sapling.entity.Torrent;
import com.github.bitsapling.sapling.repository.TorrentRepository;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TorrentService {
    @Autowired
    private TorrentRepository torrentRepository;
    @Autowired
    private CategoryService categoryService;

    @Nullable
    public Torrent getTorrent(long id) {
        return torrentRepository.findById(id).orElse(null);
    }

    @Nullable
    public Torrent getTorrent(@NotNull String infoHash) {
        infoHash = infoHash.toLowerCase();
        Optional<Torrent> entity = torrentRepository.findByInfoHashIgnoreCase(infoHash);
        return entity.orElse(null);
    }

    public List<Torrent> getAllTorrents(){
        List<Torrent> torrents = new ArrayList<>();
        torrentRepository.findAll().forEach(torrents::add);
        return torrents;
    }

    @Nullable
    public List<Torrent> getTorrentWithCategory(@Nullable String categorySlug) {
        List<Torrent> torrents = new ArrayList<>();
        if (categorySlug == null) {
            torrentRepository.findAll().forEach(torrents::add);
        } else {
            Category category = categoryService.getCategory(categorySlug);
            if (category != null) {
                torrents.addAll(torrentRepository.findAllByCategory(category));
            }
        }
        return torrents;
    }

    @NotNull
    public Torrent save(@NotNull Torrent torrent) {
        torrent.setInfoHash(torrent.getInfoHash());
        return torrentRepository.save(torrent);
    }

}
