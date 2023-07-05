package com.github.bitsapling.sapling.module.torrent;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.github.bitsapling.sapling.module.common.CommonService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTorrentMetadataService extends ServiceImpl<UserTorrentMetadataMapper, UserTorrentMetadata> implements CommonService<UserTorrentMetadata> {
    @Nullable
    public UserTorrentMetadata getUserTorrentMetadataByUserId(@NotNull Long userId) {
        return ChainWrappers.lambdaQueryChain(baseMapper)
                .eq(UserTorrentMetadata::getUser, userId)
                .one();
    }

    @Nullable
    public List<UserTorrentMetadata> getUserTorrentMetadataByTorrent(@NotNull Long torrentId) {
        return ChainWrappers.lambdaQueryChain(baseMapper)
                .eq(UserTorrentMetadata::getTorrent, torrentId)
                .list();
    }
}
