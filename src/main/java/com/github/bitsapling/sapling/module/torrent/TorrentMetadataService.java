package com.github.bitsapling.sapling.module.torrent;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.github.bitsapling.sapling.module.common.CommonService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TorrentMetadataService extends ServiceImpl<TorrentMetadataMapper, TorrentMetadata> implements CommonService<TorrentMetadata> {
    @NotNull
    public List<TorrentMetadata> getAllTorrentMetadata() {
        return ChainWrappers.lambdaQueryChain(TorrentMetadata.class).list();
    }

    @Nullable
    public TorrentMetadata getTorrentMetadata(@NotNull Long id) {
        return ChainWrappers.lambdaQueryChain(TorrentMetadata.class)
                .eq(TorrentMetadata::getId, id)
                .one();
    }

    @Nullable
    public TorrentMetadata getTorrentMetadataByTorrentId(@NotNull Long torrentId) {
        return ChainWrappers.lambdaQueryChain(TorrentMetadata.class)
                .eq(TorrentMetadata::getTorrent, torrentId)
                .one();
    }
}
