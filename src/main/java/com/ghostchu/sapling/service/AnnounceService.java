package com.ghostchu.sapling.service;

import com.ghostchu.sapling.domain.model.Peer;
import com.ghostchu.sapling.domain.model.Torrent;
import com.ghostchu.sapling.domain.model.User;
import com.ghostchu.sapling.repository.PeerRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class AnnounceService {
    private final Logger LOG = LoggerFactory.getLogger(AnnounceService.class);
    private final RedisTemplate<Object, Object> redis;
    private final PeerRepository peerRepository;
    private final List<String> badClientKeywords = List.of(
            "Mozilla",
            "WebKit",
            "Edg",
            "Chrome",
            "KHTML",
            "Gecko",
            "Firefox",
            "Opera",
            "MSIE",
            "Lynx",
            "Opera"
    );

    public AnnounceService(@Autowired RedisTemplate<Object, Object> redis, @Autowired PeerRepository peerRepository) {
        this.redis = redis;
        this.peerRepository = peerRepository;
    }

    public boolean isBitTorrentClient(String userAgent) {
        for (String badClientKeyword : badClientKeywords) {
            if (userAgent.contains(badClientKeyword)) {
                return false;
            }
        }
        return true;
    }

    public boolean isPortBanned(int port) {
        return false;
    }

    public boolean isAllowedClient(String userAgent) {
        return true;
    }

    public boolean isUserDownloadPrivilegeDisabled(User usr) {
        return false;
    }

    @Nullable
    public String getDownloadPrivilegeDisableReason(User usr) {
        return "Over speed";
    }

    @NotNull
    public List<Peer> fetchPeers(@NotNull Peer fetcher, @NotNull Torrent torrent, boolean onlyLeech, int max) {
        return peerRepository.randomFetchTorrents(torrent.getTorrentId(), onlyLeech, max);
    }

    @NotNull
    public Collection<Peer> fetchUserTorrentSeedingPeers(@NotNull Torrent torrent, @NotNull User user) {
        return peerRepository.findAllByTorrentIdAndUserId(torrent.getTorrentId(), user.getUserId());
    }

    public boolean isCooldownHit(@NotNull Torrent torrent, @NotNull Peer peer) {
        return false;
    }

    public void markCooldown(@NotNull Torrent torrent, @NotNull Peer peer) {

    }

    public boolean isOverSpeed(long time, long bytes) {
        return false;
    }

    /**
     * A peer start downloading on torrent
     *
     * @param peer The peer
     */
    @NotNull
    public Peer start(@NotNull Peer peer) {
        return peerRepository.save(peer);
    }

    /**
     * A peer completed the download on torrent
     *
     * @param peer The peer
     */
    public @NotNull Peer completed(@NotNull Peer peer) {
        return peerRepository.save(peer);
    }

    /**
     * A peer stopped download or seeding on torrent
     *
     * @param peer The peer
     */
    public void stopped(@NotNull Peer peer) {
        peerRepository.delete(peer);
    }

    @Nullable
    public List<Peer> getPeers(@NotNull Torrent torrent) {
        return peerRepository.findAllByTorrentId(torrent.getTorrentId());
    }

    @Nullable
    public Peer getPeer(@NotNull User user, @NotNull Torrent torrent, @NotNull String peerId) {
        Optional<Peer> peer = peerRepository.findByUserIdAndTorrentIdAndPeerId(user.getUserId(), torrent.getTorrentId(), peerId);
        return peer.orElse(null);
    }
}
