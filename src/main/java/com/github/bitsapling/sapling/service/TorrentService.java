package com.github.bitsapling.sapling.service;

import com.github.bitsapling.sapling.entity.Torrent;
import com.github.bitsapling.sapling.repository.TorrentRepository;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class TorrentService {
    @Autowired
    private TorrentRepository torrentRepository;

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

    @NotNull
    public Torrent save(@NotNull Torrent torrent) {
        torrent.setInfoHash(torrent.getInfoHash());
        return torrentRepository.save(torrent);
    }

}
