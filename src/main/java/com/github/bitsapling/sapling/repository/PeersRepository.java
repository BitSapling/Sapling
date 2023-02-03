package com.github.bitsapling.sapling.repository;

import com.github.bitsapling.sapling.entity.PeerEntity;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface PeersRepository extends CrudRepository<PeerEntity, Long> {
    Optional<PeerEntity> findByIpAndPortAndInfoHash(@NotNull String ip, int port, @NotNull String infoHash);
    Optional<PeerEntity> findByPeerIdAndInfoHash(@NotNull String peerId, @NotNull String infoHash);
    List<PeerEntity> findPeersByInfoHash(@NotNull String infoHash);
    List<PeerEntity> findPeersByUserId(long userId);
    void deletePeerByInfoHashAndPeerId(String infoHash, String peerId);
}
