package com.github.bitsapling.sapling.module.torrent;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.github.bitsapling.sapling.module.common.CommonService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TorrentCommentService extends ServiceImpl<TorrentCommentMapper, TorrentComment> implements CommonService<TorrentComment> {
    @NotNull
    public List<TorrentComment> getTorrentCommentsByTorrentId(@NotNull Long torrentId) {
        return ChainWrappers.lambdaQueryChain(TorrentComment.class)
                .eq(TorrentComment::getTorrent, torrentId)
                .list();
    }

    @NotNull
    public List<TorrentComment> getTorrentCommentsByReplyTo(@NotNull Long replyTo) {
        return ChainWrappers.lambdaQueryChain(TorrentComment.class)
                .eq(TorrentComment::getReplyTo, replyTo)
                .list();
    }

    @NotNull
    public List<TorrentComment> getTorrentCommentsByOwner(@NotNull Long owner) {
        return ChainWrappers.lambdaQueryChain(TorrentComment.class)
                .eq(TorrentComment::getOwner, owner)
                .list();
    }
}
