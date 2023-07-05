package com.github.bitsapling.sapling.module.tracker;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.github.bitsapling.sapling.module.common.CommonService;
import com.github.bitsapling.sapling.module.torrent.Torrent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PeerService extends ServiceImpl<PeerMapper, Peer> implements CommonService<Peer> {
    @NotNull
    public List<Peer> getPeersByPeerId(byte[] peerId) {
        return ChainWrappers.lambdaQueryChain(Peer.class)
                .eq(Peer::getPeerId, peerId).list();
    }

    @NotNull
    public List<Peer> getPeersByUser(Long userId) {
        return ChainWrappers.lambdaQueryChain(Peer.class)
                .eq(Peer::getUser, userId).list();
    }

    @NotNull
    public List<Peer> getPeersByTorrent(Long torrentId) {
        return ChainWrappers.lambdaQueryChain(Peer.class)
                .eq(Peer::getTorrent, torrentId).list();
    }

    @NotNull
    public List<Peer> getPeersByTorrent(Long torrentId, int limit) {
        return ChainWrappers.lambdaQueryChain(Peer.class)
                .eq(Peer::getTorrent, torrentId)
                .last("LIMIT " + limit).list();
    }

    @NotNull
    public List<Peer> getPeersByTorrentRandom(Long torrentId, int limit) {
        return ChainWrappers.lambdaQueryChain(Peer.class)
                .eq(Peer::getTorrent, torrentId)
                .last("ORDER BY RAND() LIMIT " + limit).list();
    }


    public boolean deletePeersByTorrentId(@NotNull Long torrentId) {
        return ChainWrappers.lambdaUpdateChain(Peer.class)
                .eq(Peer::getTorrent, torrentId).remove();
    }

    public boolean deletePeersByUserId(@NotNull Long userId) {
        return ChainWrappers.lambdaUpdateChain(Peer.class)
                .eq(Peer::getUser, userId).remove();
    }

    public boolean deletePeersInactive(@NotNull LocalDateTime earlyThan) {
        return ChainWrappers.lambdaUpdateChain(Peer.class)
                .le(Peer::getLastAction, earlyThan).remove();
    }

    public List<Peer> getPeers(Torrent torrent) {
        return ChainWrappers.lambdaQueryChain(Peer.class)
                .eq(Peer::getTorrent, torrent.getId()).list();
    }
}
