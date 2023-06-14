package com.github.bitsapling.sapling.module.torrent;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TorrentCommentServiceImpl extends ServiceImpl<TorrentCommentMapper, TorrentComment> implements TorrentCommentService {
    @NotNull
    public List<TorrentComment> getTorrentCommentsByTorrentId(@NotNull Long torrentId) {
        LambdaQueryWrapper<TorrentComment> wrapper = Wrappers.lambdaQuery(TorrentComment.class);
        wrapper.eq(TorrentComment::getTorrent, torrentId);
        return baseMapper.selectList(wrapper);
    }

    @NotNull
    public List<TorrentComment> getTorrentCommentsByReplyTo(@NotNull Long replyTo) {
        LambdaQueryWrapper<TorrentComment> wrapper = Wrappers.lambdaQuery(TorrentComment.class);
        wrapper.eq(TorrentComment::getReplyTo, replyTo);
        return baseMapper.selectList(wrapper);
    }

    @NotNull
    public List<TorrentComment> getTorrentCommentsByOwner(@NotNull Long owner) {
        LambdaQueryWrapper<TorrentComment> wrapper = Wrappers.lambdaQuery(TorrentComment.class);
        wrapper.eq(TorrentComment::getOwner, owner);
        return baseMapper.selectList(wrapper);
    }
}
