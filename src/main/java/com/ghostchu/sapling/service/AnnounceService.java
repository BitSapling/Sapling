package com.ghostchu.sapling.service;

import com.ghostchu.sapling.domain.entity.Peer;
import com.ghostchu.sapling.domain.entity.Torrent;
import com.ghostchu.sapling.domain.entity.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class AnnounceService {
    private final Logger LOG = LoggerFactory.getLogger(AnnounceService.class);
    private final RedisTemplate<Object, Object> redis;
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

    public AnnounceService(@Autowired RedisTemplate<Object, Object> redis) {
        this.redis = redis;
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

    }

    @NotNull
    public Collection<Peer> fetchUserTorrentSeedingPeers(@NotNull Torrent torrent, @NotNull User user) {

    }

    public boolean isCooldownHit(@NotNull Torrent torrent, @NotNull Peer peer) {

    }

    public void markCooldown(@NotNull Torrent torrent, @NotNull Peer peer) {

    }

    public boolean isOverSpeed(long time, long bytes) {
        return false;
    }

    /**
     * A peer start downloading on torrent
     *
     * @param peer    The peer
     * @param torrent The torrent
     */
    public void start(@NotNull Peer peer, @NotNull Torrent torrent) {

    }

    /**
     * A peer completed the download on torrent
     *
     * @param peer    The peer
     * @param torrent The torrent
     */
    public void completed(@NotNull Peer peer, @NotNull Torrent torrent) {

    }

    /**
     * A peer stopped download or seeding on torrent
     *
     * @param peer    The peer
     * @param torrent The torrent
     */
    public void stopped(@NotNull Peer peer, @NotNull Torrent torrent) {
        t
    }

    @Nullable
    public Peer getPeer(User user, Torrent torrent, String peerId) {

    }
}
