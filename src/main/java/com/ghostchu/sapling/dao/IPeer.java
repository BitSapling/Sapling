package com.ghostchu.sapling.dao;

import com.ghostchu.sapling.domain.entity.Peer;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPeer extends CrudRepository<Peer, Long> {
    @Override
    Optional<Peer> findById(@NotNull Long id);

}
