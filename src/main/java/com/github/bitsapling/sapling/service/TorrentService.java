package com.github.bitsapling.sapling.service;

import com.github.bitsapling.sapling.entity.TorrentEntity;
import com.github.bitsapling.sapling.objects.Torrent;
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
        TorrentEntity entity = torrentRepository.findById(id).orElse(null);
        if (entity == null) return null;
        return convert(entity);
    }

    @Nullable
    @Cacheable(cacheNames ={"torrent"}, key = "#infoHash")
    public Torrent getTorrent(@NotNull String infoHash) {
        Optional<TorrentEntity> entity = torrentRepository.findByInfoHash(infoHash);
        return entity.map(this::convert).orElse(null);
    }

    public void save(@NotNull Torrent torrent) {
        torrentRepository.save(convert(torrent));
    }

    @NotNull
    public Torrent convert(@NotNull TorrentEntity entity) {
        return new Torrent(
                entity.getId(),
                entity.getInfoHash(),
                userService.convert(entity.getUser()),
                entity.getTitle(),
                entity.getSubTitle(),
                entity.getSize(),
                entity.getFinishes(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.isUnderReview(),
                entity.isAnonymous(),
                entity.getType(),
                promotionService.convert(entity.getPromotionPolicy()),
                entity.getDescriptionType(),
                entity.getDescription()
        );
    }

    @NotNull
    public TorrentEntity convert(@NotNull Torrent torrent) {
        return new TorrentEntity(
                torrent.getId(),
                torrent.getInfoHash(),
                userService.convert(torrent.getUser()),
                torrent.getTitle(),
                torrent.getSubTitle(),
                torrent.getSize(),
                torrent.getFinishes(),
                torrent.getCreatedAt(),
                torrent.getUpdatedAt(),
                torrent.isUnderReview(),
                torrent.isAnonymous(),
                torrent.getType(),
                promotionService.convert(torrent.getPromotionPolicy()),
                torrent.getDescriptionType(),
                torrent.getDescription()
        );
    }
}
