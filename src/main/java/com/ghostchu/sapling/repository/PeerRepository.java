package com.ghostchu.sapling.repository;

import com.ghostchu.sapling.domain.model.Peer;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeerRepository extends JpaRepository<Peer, Long> {
    @NotNull
    Optional<Peer> findByIpv4(@NotNull String ipv4);

    @NotNull
    Optional<Peer> findByIpv6(@NotNull String ipv6);

    @NotNull
    Optional<Peer> findByUserId(@NotNull Long userId);

    @NotNull
    List<Peer> findAllByTorrentId(@NotNull Long torrentId);

    @Query(value = "select * from `peers` where `torrent_id`=?1 and `seeder`=?2 order by rand() limit ?3",
            nativeQuery = true)
    List<Peer> randomFetchTorrents(@NotNull Long torrentId, boolean onlyLeech, int max);

    @NotNull
    List<Peer> findAllByTorrentIdAndUserId(@NotNull Long torrentId, @NotNull Long userId);
}
