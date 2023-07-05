package com.github.bitsapling.sapling.module.torrent;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.github.bitsapling.sapling.module.common.CommonService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TorrentService extends ServiceImpl<TorrentMapper, Torrent> implements CommonService<Torrent> {
    @Autowired
    private TorrentMapper mapper;

    @Nullable
    public Torrent getTorrentByInfoHash(@NotNull String infoHash) {
        return ChainWrappers.lambdaQueryChain(Torrent.class)
                .eq(Torrent::getInfoHashV1, infoHash)
                .or(wrapper -> wrapper.eq(Torrent::getInfoHashV2, infoHash))
                .one();
    }

    @NotNull
    public List<Torrent> getTorrentsByUploader(@NotNull Long uploader) {
        return ChainWrappers.lambdaQueryChain(Torrent.class)
                .eq(Torrent::getUploader, uploader)
                .list();
    }

    @NotNull
    public List<Torrent> getTorrentsByCategory(@NotNull Long categoryId) {
        return ChainWrappers.lambdaQueryChain(Torrent.class)
                .eq(Torrent::getCategory, categoryId)
                .list();
    }

    @NotNull
    public List<Torrent> searchTorrents(@NotNull String keyword) {
        return ChainWrappers.lambdaQueryChain(Torrent.class)
                .like(Torrent::getTitle, keyword)
                .or(wrapper -> wrapper.like(Torrent::getSubTitle, keyword))
                .list();
    }
}
