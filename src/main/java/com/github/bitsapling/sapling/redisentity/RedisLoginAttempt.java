package com.github.bitsapling.sapling.redisentity;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.sql.Timestamp;

@RedisHash(value = "login_attempt", timeToLive = 900)
@Data
public class RedisLoginAttempt {
    @Id
    private String ip;
    @Indexed
    private long attempts;
    @Indexed
    private Timestamp lastAttempt;
}
