package com.github.bitsapling.sapling.redisrepository;

import com.github.bitsapling.sapling.redisentity.RedisLoginAttempt;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RedisLoginAttemptRepository extends CrudRepository<RedisLoginAttempt, String> {
    Optional<RedisLoginAttempt> findByIp(@NotNull String ip);
    void deleteByIp(@NotNull String ip);
}
