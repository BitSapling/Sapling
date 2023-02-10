package com.github.bitsapling.sapling.repository;

import com.github.bitsapling.sapling.entity.Peer;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface PeersRepository extends CrudRepository<Peer, Long> {
    Optional<Peer> findByIpAndPortAndInfoHashIgnoreCase(@NotNull String ip, int port, @NotNull String infoHash);

    Optional<Peer> findByPeerIdAndInfoHashIgnoreCase(@NotNull String peerId, @NotNull String infoHash);

    List<Peer> findPeersByInfoHashIgnoreCaseOrderByUpdateAtDesc(@NotNull String infoHash, @NotNull Pageable singlePage);

    //List<PeerEntity> findPeersByUserId(long userId);
    List<Peer> findAllByUpdateAtIsLessThan(@NotNull Timestamp timestamp);

    void deletePeerByInfoHashIgnoreCaseAndPeerId(String infoHash, String peerId);
}
