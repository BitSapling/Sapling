package com.github.bitsapling.sapling.service;

import com.github.bitsapling.sapling.entity.User;
import com.github.bitsapling.sapling.redisentity.RedisLoginAttempt;
import com.github.bitsapling.sapling.redisrepository.RedisLoginAttemptRepository;
import com.github.bitsapling.sapling.type.LoginType;
import com.github.bitsapling.sapling.util.IPUtil;
import com.github.bitsapling.sapling.util.PasswordHash;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Service
@Slf4j
public class AuthenticationService {
    @Autowired
    private UserService userService;
    @Autowired
    private LoginHistoryService loginHistoryService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private RedisLoginAttemptRepository repository;

    public boolean authenticate(@NotNull User user, @NotNull String password, @Nullable String ipAddress) {
        boolean verify = PasswordHash.verify(password, user.getPasswordHash());
        if(StringUtils.isEmpty(ipAddress)){
            ipAddress = IPUtil.getRequestIp(request);
        }
        if (verify) {
            cleanUserLoginFail(ipAddress);
            loginHistoryService.log(user, LoginType.ACCOUNT, ipAddress, request.getHeader("User-Agent"));
        } else {
            markUserLoginFail(ipAddress);
        }
        return verify;
    }

    @Nullable
    public User authenticate(@NotNull String passkey, @Nullable String ipAddress) {
        User user = userService.getUserByPasskey(passkey);
        if(StringUtils.isEmpty(ipAddress)){
            ipAddress = IPUtil.getRequestIp(request);
        }
        if (user != null) {
            cleanUserLoginFail(ipAddress);
            loginHistoryService.log(user, LoginType.PASSKEY, ipAddress, request.getHeader("User-Agent"));
        } else {
            markUserLoginFail(ipAddress);
        }
        return user;
    }

    public void cleanUserLoginFail(@Nullable String ip) {
        if (ip == null) {
            log.warn("Failed to clean user login fail because ip is null", new IllegalArgumentException("IP is null"));
            return;
        }
        repository.deleteByIp(ip);
    }

    public long markUserLoginFail(@Nullable String ip) {
        if (ip == null) {
            log.warn("Failed to mark user login fail because ip is null", new IllegalArgumentException("IP is null"));
            return 0;
        }
        Optional<RedisLoginAttempt> optional = repository.findByIp(ip);
        RedisLoginAttempt loginAttempt;
        if (optional.isPresent()) {
            loginAttempt = optional.get();
            loginAttempt.setAttempts(loginAttempt.getAttempts() + 1);
        } else {
            loginAttempt = new RedisLoginAttempt();
            loginAttempt.setIp(ip);
        }
        loginAttempt.setLastAttempt(Timestamp.from(Instant.now()));
        repository.save(loginAttempt);
        return loginAttempt.getAttempts();
    }
}
