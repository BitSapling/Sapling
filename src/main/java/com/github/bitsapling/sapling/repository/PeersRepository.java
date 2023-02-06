package com.github.bitsapling.sapling.repository;

import com.github.bitsapling.sapling.entity.Peer;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface PeersRepository extends CrudRepository<Peer, Long> {
    Optional<Peer> findByIpAndPortAndInfoHash(@NotNull String ip, int port, @NotNull String infoHash);
    Optional<Peer> findByPeerIdAndInfoHash(@NotNull String peerId, @NotNull String infoHash);
    List<Peer> findPeersByInfoHash(@NotNull String infoHash);
    //List<PeerEntity> findPeersByUserId(long userId);
    List<Peer> findAllByUpdateAtLessThan(@NotNull Instant instant);
    void deletePeerByInfoHashAndPeerId(String infoHash, String peerId);
}
