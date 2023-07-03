package com.github.bitsapling.sapling.module.torrent;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.bitsapling.sapling.module.common.CommonService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TorrentCommentService extends ServiceImpl<TorrentCommentMapper, TorrentComment> implements CommonService<TorrentComment> {
    @NotNull
    public List<TorrentComment> getTorrentCommentsByTorrentId(@NotNull Long torrentId) {
        return baseMapper.selectList(lambdaQuery().eq(TorrentComment::getTorrent, torrentId));
    }

    @NotNull
    public List<TorrentComment> getTorrentCommentsByReplyTo(@NotNull Long replyTo) {
        return baseMapper.selectList(lambdaQuery().eq(TorrentComment::getReplyTo, replyTo));
    }

    @NotNull
    public List<TorrentComment> getTorrentCommentsByOwner(@NotNull Long owner) {
        return baseMapper.selectList(lambdaQuery().eq(TorrentComment::getOwner, owner));
    }
}
