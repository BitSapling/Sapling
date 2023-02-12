package com.github.bitsapling.sapling.service;

import com.github.bitsapling.sapling.entity.LoginHistory;
import com.github.bitsapling.sapling.entity.User;
import com.github.bitsapling.sapling.repository.LoginHistoryRepository;
import com.github.bitsapling.sapling.type.LoginType;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service

public class LoginHistoryService {
    @Autowired
    private LoginHistoryRepository repository;

    @NotNull
    public LoginHistory log(@NotNull User user, @NotNull LoginType loginType, @NotNull String ip, @NotNull String userAgent) {
        LoginHistory history = new LoginHistory(0, user, Timestamp.from(Instant.now()),
                loginType, ip, userAgent, "Unknown - GeoIP not initialized");
        return repository.save(history);
    }
}
