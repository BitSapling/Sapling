package com.github.bitsapling.sapling.module.tracker;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public abstract class PeerService implements IService<Peer> {
    @Autowired
    private PeerMapper mapper;

    @NotNull
    public List<Peer> getPeersByPeerId(byte[] peerId) {
        LambdaQueryWrapper<Peer> wrapper = Wrappers
                .lambdaQuery(Peer.class)
                .eq(Peer::getPeerId, peerId);
        return mapper.selectList(wrapper);
    }

    @NotNull
    public List<Peer> getPeersByUser(Long userId) {
        LambdaQueryWrapper<Peer> wrapper = Wrappers
                .lambdaQuery(Peer.class)
                .eq(Peer::getUser, userId);
        return mapper.selectList(wrapper);
    }

    @NotNull
    public List<Peer> getPeersByTorrent(Long torrentId) {
        LambdaQueryWrapper<Peer> wrapper = Wrappers
                .lambdaQuery(Peer.class)
                .eq(Peer::getTorrent, torrentId);
        return mapper.selectList(wrapper);
    }

    @NotNull
    public List<Peer> getPeersByTorrent(Long torrentId, int limit) {
        LambdaQueryWrapper<Peer> wrapper = Wrappers
                .lambdaQuery(Peer.class)
                .eq(Peer::getTorrent, torrentId)
                .last("LIMIT " + limit);
        return mapper.selectList(wrapper);
    }

    @NotNull
    public List<Peer> getPeersByTorrentRandom(Long torrentId, int limit) {
        LambdaQueryWrapper<Peer> wrapper = Wrappers
                .lambdaQuery(Peer.class)
                .eq(Peer::getTorrent, torrentId)
                .last("ORDER BY RAND() LIMIT " + limit);
        return mapper.selectList(wrapper);
    }


    public void deletePeersByTorrentId(@NotNull Long torrentId) {
        mapper.delete(Wrappers.lambdaQuery(Peer.class)
                .eq(Peer::getTorrent, torrentId));
    }

    public void deletePeersByUserId(@NotNull Long userId) {
        mapper.delete(Wrappers.lambdaQuery(Peer.class)
                .eq(Peer::getUser, userId));
    }

    public void deletePeersInactive(@NotNull LocalDateTime earlyThan) {
        mapper.delete(Wrappers.lambdaQuery(Peer.class)
                .le(Peer::getLastAction, earlyThan));
    }
}
