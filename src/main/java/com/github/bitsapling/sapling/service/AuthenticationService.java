package com.github.bitsapling.sapling.service;

import com.github.bitsapling.sapling.config.SecurityConfig;
import com.github.bitsapling.sapling.entity.User;
import com.github.bitsapling.sapling.exception.APIErrorCode;
import com.github.bitsapling.sapling.exception.APIGenericException;
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
    @Autowired
    private SettingService settingService;

    private SecurityConfig getSecurityConfig() {
        return settingService.get(SecurityConfig.getConfigKey(), SecurityConfig.class);
    }

    public boolean authenticate(@NotNull User user, @NotNull String password, @Nullable String ipAddress) {
        checkAccountLoginAttempts(ipAddress);
        boolean verify = PasswordHash.verify(password, user.getPasswordHash());
        if (StringUtils.isEmpty(ipAddress)) {
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
        checkPasskeyLoginAttempts(ipAddress);
        User user = userService.getUserByPasskey(passkey);
        if (StringUtils.isEmpty(ipAddress)) {
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
        if(ip == null) return;
        repository.deleteByIp(ip);
    }

    public long markUserLoginFail(@Nullable String ip) {
        if(ip == null) return 0;
        Optional<RedisLoginAttempt> optional = repository.findByIp(ip);
        RedisLoginAttempt loginAttempt;
        if (optional.isPresent()) {
            loginAttempt = optional.get();
            loginAttempt.setAttempts(loginAttempt.getAttempts() + 1);
        } else {
            loginAttempt = new RedisLoginAttempt();
            loginAttempt.setIp(ip);
        }
        loginAttempt.setLastAttempt(System.currentTimeMillis());
        loginAttempt = repository.save(loginAttempt);
        return loginAttempt.getAttempts();
    }

    public void checkAccountLoginAttempts(@Nullable String ip){
        if(ip == null) return;
        if (getUserFail(ip) > getSecurityConfig().getMaxAuthenticationAttempts()) {
            throw new APIGenericException(APIErrorCode.TOO_MANY_FAILED_AUTHENTICATION_ATTEMPTS, "Too many failed login attempts");
        }
    }
    public void checkPasskeyLoginAttempts(@Nullable String ip){
        if(ip == null) return;
        if (getUserFail(ip) > getSecurityConfig().getMaxPasskeyAuthenticationAttempts()) {
            throw new APIGenericException(APIErrorCode.TOO_MANY_FAILED_AUTHENTICATION_ATTEMPTS, "Too many failed login attempts");
        }
    }

    public long getUserFail(@Nullable String ip) {
        if (ip == null) return 0;
        Optional<RedisLoginAttempt> optional = repository.findByIp(ip);
        long attempts = optional.map(RedisLoginAttempt::getAttempts).orElse(0L);
        return attempts;
    }
}
