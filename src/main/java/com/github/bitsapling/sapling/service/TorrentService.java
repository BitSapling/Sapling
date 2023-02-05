package com.github.bitsapling.sapling.service;

import com.github.bitsapling.sapling.entity.TorrentEntity;
import com.github.bitsapling.sapling.objects.Torrent;
import com.github.bitsapling.sapling.repository.TorrentRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class TorrentService {
    @Autowired
    private TorrentRepository torrentRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PromotionService promotionService;

    @Nullable
    @Cacheable(cacheNames = "torrent", key = "#id")
    public Torrent getTorrent(long id) {
        TorrentEntity entity = torrentRepository.findById(id).orElse(null);
        if (entity == null) return null;
        return convert(entity);
    }
    @Nullable
    @Cacheable(cacheNames = "torrent", key = "'info_hash='.concat(#infoHash)")
    public Torrent getTorrent(@NotNull String infoHash){
        Optional<TorrentEntity> entity = torrentRepository.findByInfoHash(infoHash);
        return entity.map(this::convert).orElse(null);
    }
   @Caching(evict = {
           @CacheEvict(cacheNames = "torrent", key = "#torrent.id"),
           @CacheEvict(cacheNames = "torrent", key = "'info_hash='.concat(#torrent.infoHash)")
   })
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
                entity.getCreatedAt().toInstant(),
                entity.getUpdatedAt().toInstant(),
                entity.isDraft(),
                entity.isUnderReview(),
                entity.isDeleted(),
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
                Timestamp.from(torrent.getCreatedAt()),
                Timestamp.from(torrent.getUpdatedAt()),
                torrent.isDraft(),
                torrent.isUnderReview(),
                torrent.isDeleted(),
                torrent.isAnonymous(),
                torrent.getType(),
                promotionService.convert(torrent.getPromotionPolicy()),
                torrent.getDescriptionType(),
                torrent.getDescription()
        );
    }
}
