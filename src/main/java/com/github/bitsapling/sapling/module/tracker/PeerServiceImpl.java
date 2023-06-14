package com.github.bitsapling.sapling.module.tracker;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PeerServiceImpl extends ServiceImpl<PeerMapper, Peer> implements PeerService {
    @NotNull
    public List<Peer> getPeersByPeerId(byte[] peerId) {
        LambdaQueryWrapper<Peer> wrapper = Wrappers
                .lambdaQuery(Peer.class)
                .eq(Peer::getPeerId, peerId);
        return baseMapper.selectList(wrapper);
    }

    @NotNull
    public List<Peer> getPeersByUser(Long userId) {
        LambdaQueryWrapper<Peer> wrapper = Wrappers
                .lambdaQuery(Peer.class)
                .eq(Peer::getUser, userId);
        return baseMapper.selectList(wrapper);
    }

    @NotNull
    public List<Peer> getPeersByTorrent(Long torrentId) {
        LambdaQueryWrapper<Peer> wrapper = Wrappers
                .lambdaQuery(Peer.class)
                .eq(Peer::getTorrent, torrentId);
        return baseMapper.selectList(wrapper);
    }

    @NotNull
    public List<Peer> getPeersByTorrent(Long torrentId, int limit) {
        LambdaQueryWrapper<Peer> wrapper = Wrappers
                .lambdaQuery(Peer.class)
                .eq(Peer::getTorrent, torrentId)
                .last("LIMIT " + limit);
        return baseMapper.selectList(wrapper);
    }

    @NotNull
    public List<Peer> getPeersByTorrentRandom(Long torrentId, int limit) {
        LambdaQueryWrapper<Peer> wrapper = Wrappers
                .lambdaQuery(Peer.class)
                .eq(Peer::getTorrent, torrentId)
                .last("ORDER BY RAND() LIMIT " + limit);
        return baseMapper.selectList(wrapper);
    }


    public void deletePeersByTorrentId(@NotNull Long torrentId) {
        baseMapper.delete(Wrappers.lambdaQuery(Peer.class)
                .eq(Peer::getTorrent, torrentId));
    }

    public void deletePeersByUserId(@NotNull Long userId) {
        baseMapper.delete(Wrappers.lambdaQuery(Peer.class)
                .eq(Peer::getUser, userId));
    }

    public void deletePeersInactive(@NotNull LocalDateTime earlyThan) {
        baseMapper.delete(Wrappers.lambdaQuery(Peer.class)
                .le(Peer::getLastAction, earlyThan));
    }
}
