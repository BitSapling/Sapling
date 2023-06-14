package com.github.bitsapling.sapling.module.torrent;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public abstract class TorrentService implements IService<Torrent> {
    @Autowired
    private TorrentMapper mapper;

    @Nullable
    public Torrent getTorrentByInfoHash(@NotNull String infoHash) {
        return mapper.selectOne(Wrappers.lambdaQuery(Torrent.class)
                .eq(Torrent::getInfoHashV1, infoHash)
                .or(wrapper -> wrapper.eq(Torrent::getInfoHashV2, infoHash)));
    }

    @NotNull
    public List<Torrent> getTorrentsByUploader(@NotNull Long uploader) {
        return mapper.selectList(Wrappers.lambdaQuery(Torrent.class)
                .eq(Torrent::getUploader, uploader));
    }

    @NotNull
    public List<Torrent> getTorrentsByCategory(@NotNull Long categoryId) {
        return mapper.selectList(Wrappers.lambdaQuery(Torrent.class)
                .eq(Torrent::getCategory, categoryId));
    }

    @NotNull
    public List<Torrent> searchTorrents(@NotNull String keyword) {
        return mapper.selectList(Wrappers.lambdaQuery(Torrent.class)
                .like(Torrent::getTitle, keyword)
                .or(wrapper -> wrapper.like(Torrent::getSubTitle, keyword)));
    }
}
