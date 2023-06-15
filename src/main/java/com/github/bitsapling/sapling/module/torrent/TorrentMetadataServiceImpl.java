package com.github.bitsapling.sapling.module.torrent;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.bitsapling.sapling.module.common.CommonService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TorrentMetadataServiceImpl extends ServiceImpl<TorrentMetadataMapper, TorrentMetadata> implements CommonService<TorrentMetadata> {
    @NotNull
    public List<TorrentMetadata> getAllTorrentMetadata() {
        return baseMapper.selectList(Wrappers.lambdaQuery(TorrentMetadata.class));
    }

    @Nullable
    public TorrentMetadata getTorrentMetadata(@NotNull Long id) {
        return baseMapper.selectById(id);
    }

    @Nullable
    public TorrentMetadata getTorrentMetadataByTorrentId(@NotNull Long torrentId) {
        LambdaQueryWrapper<TorrentMetadata> wrapper = Wrappers
                .lambdaQuery(TorrentMetadata.class)
                .eq(TorrentMetadata::getTorrent, torrentId);
        return baseMapper.selectOne(wrapper);
    }
}
