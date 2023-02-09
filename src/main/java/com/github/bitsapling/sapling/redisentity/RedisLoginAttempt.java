package com.github.bitsapling.sapling.redisentity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash(value = "login_attempt", timeToLive = 900)
@Data
public class RedisLoginAttempt {
    @Id
    @Indexed
    private String ip;
    @Indexed
    private long attempts;
    @Indexed
    private long lastAttempt;
}
