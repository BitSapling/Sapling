package com.github.bitsapling.sapling.service;

import com.github.bitsapling.sapling.entity.Torrent;
import com.github.bitsapling.sapling.entity.TransferHistory;
import com.github.bitsapling.sapling.entity.User;
import com.github.bitsapling.sapling.repository.TransferHistoryRepository;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class TransferHistoryService {
    @Autowired
    private TransferHistoryRepository repository;

    @Nullable
    public TransferHistory getTransferHistory(@NotNull User user, @NotNull Torrent torrent){
        return repository.findByUserAndTorrent(user, torrent).orElse(null);
    }
    @NotNull
    public List<TransferHistory> getTransferHistory(@NotNull Torrent torrent){
        return repository.findAllByTorrent(torrent);
    }
    @NotNull
    public TransferHistory save(@NotNull TransferHistory transferHistory){
        return repository.save(transferHistory);
    }
}
