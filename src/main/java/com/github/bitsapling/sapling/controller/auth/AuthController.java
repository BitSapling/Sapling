package com.github.bitsapling.sapling.controller.auth;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.github.bitsapling.sapling.controller.auth.dto.request.LoginRequestDTO;
import com.github.bitsapling.sapling.controller.auth.dto.request.RegisterRequestDTO;
import com.github.bitsapling.sapling.controller.dto.response.LoginStatusResponseDTO;
import com.github.bitsapling.sapling.controller.dto.response.UserSessionResponseDTO;
import com.github.bitsapling.sapling.controller.dto.response.UserResponseDTO;
import com.github.bitsapling.sapling.entity.User;
import com.github.bitsapling.sapling.exception.APIErrorCode;
import com.github.bitsapling.sapling.exception.APIGenericException;
import com.github.bitsapling.sapling.service.UserGroupService;
import com.github.bitsapling.sapling.service.UserService;
import com.github.bitsapling.sapling.util.IPUtil;
import com.github.bitsapling.sapling.util.PasswordHash;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import static com.github.bitsapling.sapling.exception.APIErrorCode.AUTHENTICATION_FAILED;
import static com.github.bitsapling.sapling.exception.APIErrorCode.MISSING_PARAMETERS;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserService userService;
    @Autowired
    private UserGroupService userGroupService;

    @PostMapping("/login")
    public UserSessionResponseDTO login(@RequestBody LoginRequestDTO login) {
        if (StringUtils.isEmpty(login.getUser())) {
            throw new APIGenericException(MISSING_PARAMETERS, "User parameter is required");
        }
        if (StringUtils.isEmpty(login.getPassword())) {
            throw new APIGenericException(MISSING_PARAMETERS, "Password parameter is required");
        }
        User user = userService.getUserByUsername(login.getUser());
        if (user == null) user = userService.getUserByEmail(login.getUser());
        if (user == null) {
            log.warn("IP {} tried to login with not exists username {}", IPUtil.getRequestIp(request), login.getUser());
            throw new APIGenericException(AUTHENTICATION_FAILED);
        }
        if (!PasswordHash.verify(login.getPassword(), user.getPasswordHash())) {
            log.warn("IP {} tried to login with username {} and password {}", IPUtil.getRequestIp(request), login.getUser(), login.getPassword());
            throw new APIGenericException(AUTHENTICATION_FAILED, "Username or Password incorrect");
        }
        StpUtil.login(user.getId());
        return getUserBasicInformation(user);
    }

    @PostMapping("/logout")
    public Map<String, Object> logout() {
        if (StpUtil.isLogin()) {
            StpUtil.logout();
            Map<String, Object> logoutResponse = new LinkedHashMap<>();
            logoutResponse.put("status", "ok");
            return logoutResponse;
        } else {
            throw new APIGenericException(APIErrorCode.REQUIRED_AUTHENTICATION);
        }
    }

    @GetMapping("/status")
    public LoginStatusResponseDTO status() {
        try {
            User user = userService.getUser(StpUtil.getLoginIdAsLong());
            if (user == null) {
                return new LoginStatusResponseDTO(false, false, false, null);
            } else {
                return new LoginStatusResponseDTO(true, true, false, getUserBasicInformation(user));
            }
        } catch (NotLoginException e) {
            return new LoginStatusResponseDTO(false, false, false, null);
        }
    }

    @PostMapping("/register")
    public UserSessionResponseDTO register(@RequestBody RegisterRequestDTO register) {
        if (StringUtils.isEmpty(register.getEmail())) {
            throw new APIGenericException(MISSING_PARAMETERS, "Email parameter is required");
        }
        if (StringUtils.isEmpty(register.getUsername())) {
            throw new APIGenericException(MISSING_PARAMETERS, "Username parameter is required");
        }
        if (StringUtils.isEmpty(register.getPassword())) {
            throw new APIGenericException(MISSING_PARAMETERS, "Password parameter is required");
        }
        User user = userService.getUserByUsername(register.getUsername());
        if (user != null) {
            throw new APIGenericException(APIErrorCode.USERNAME_ALREADY_IN_USAGE);
        }
        user = userService.getUserByEmail(register.getEmail());
        if (user != null) {
            throw new APIGenericException(APIErrorCode.EMAIL_ALREADY_IN_USAGE);
        }
        user = userService.save(new User(
                0,
                register.getEmail(),
                PasswordHash.hash(register.getPassword()),
                register.getUsername(),
                userGroupService.getDefaultUserGroup(),
                UUID.randomUUID().toString(),
                Timestamp.from(Instant.now()),
                "https://www.baidu.com/facivon.ico",
                "测试用户",
                "这个用户很懒，还没有个性签名",
                "zh-CN",
                "100mbps",
                "100mbps",
                0L, 0L, 0L, 0L,
                "未知",
                BigDecimal.ZERO,
                0,
                0L,
                UUID.randomUUID().toString()));
        StpUtil.login(user.getId());
        return getUserBasicInformation(user);
    }

    @NotNull
    private UserSessionResponseDTO getUserBasicInformation(User user) {
        SaTokenInfo tokenInfo = null;
        try {
            tokenInfo = StpUtil.getTokenInfo();
        } catch (NotLoginException ignored) {

        }
        return new UserSessionResponseDTO(tokenInfo, new UserResponseDTO(user));
    }

}
