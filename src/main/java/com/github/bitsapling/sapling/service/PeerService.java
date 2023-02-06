package com.github.bitsapling.sapling.service;

import com.github.bitsapling.sapling.entity.Peer;
import com.github.bitsapling.sapling.repository.PeersRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Repository
@Slf4j
@Transactional
public class PeerService {
    @Autowired
    private PeersRepository repository;

    @Nullable
    public Peer getPeer(@NotNull String ip, int port, @NotNull String infoHash) {
        return repository.findByIpAndPortAndInfoHash(ip, port, infoHash).orElse(null);
    }

    @NotNull

    public List<Peer> getPeers(@NotNull String infoHash) {
        List<Peer> entities = repository.findPeersByInfoHash(infoHash);
        return entities.stream().toList();
    }


    public Peer save(@NotNull Peer peer) {
        return repository.save(peer);
    }


    public void delete(@NotNull Peer peer) {
        //repository.delete(convert(peer));
        repository.deleteById(peer.getId());
    }

    public int cleanup() {
        List<Peer> entities = repository.findAllByUpdateAtLessThan(Instant.now().minus(90, ChronoUnit.MINUTES));
        int count = entities.size();
        repository.deleteAll(entities);
        return count;
    }
}
