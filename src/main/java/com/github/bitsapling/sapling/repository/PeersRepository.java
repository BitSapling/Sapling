package com.github.bitsapling.sapling.repository;

import com.github.bitsapling.sapling.entity.Peer;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface PeersRepository extends CrudRepository<Peer, UUID> {
    Optional<Peer> findByIpAndPortAndInfoHash(@NotNull String ip, int port, @NotNull String infoHash);
    List<Peer> findPeersByInfoHash(@NotNull String infoHash);
    List<Peer> findPeersByUserId(long userId);
    List<Peer> findPeersByTorrentId(long torrentId);
}
