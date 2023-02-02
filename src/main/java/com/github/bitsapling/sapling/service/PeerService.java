package com.github.bitsapling.sapling.service;

import com.github.bitsapling.sapling.entity.Peer;
import com.github.bitsapling.sapling.repository.PeersRepository;
import com.github.bitsapling.sapling.util.RandomUtil;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Repository
@Transactional
public class PeerService {
    @Autowired
    private PeersRepository repository;

    @NotNull
    public List<Peer> fetchPeers(@NotNull String infoHash, int numWant) {
        List<Peer> allPeers = repository.findPeersByInfoHash(infoHash);
        return RandomUtil.getRandomElements(allPeers, numWant);
    }

}
