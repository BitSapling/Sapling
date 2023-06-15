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
public class UserTorrentMetadataService extends ServiceImpl<UserTorrentMetadataMapper, UserTorrentMetadata> implements CommonService<UserTorrentMetadata> {
    @Nullable
    public UserTorrentMetadata getUserTorrentMetadataByUserId(@NotNull Long userId) {
        LambdaQueryWrapper<UserTorrentMetadata> wrapper = Wrappers
                .lambdaQuery(UserTorrentMetadata.class)
                .eq(UserTorrentMetadata::getUser, userId);
        return baseMapper.selectOne(wrapper);
    }

    @Nullable
    public List<UserTorrentMetadata> getUserTorrentMetadataByTorrent(@NotNull Long torrentId) {
        LambdaQueryWrapper<UserTorrentMetadata> wrapper = Wrappers
                .lambdaQuery(UserTorrentMetadata.class)
                .eq(UserTorrentMetadata::getTorrent, torrentId);
        return baseMapper.selectList(wrapper);
    }
}
