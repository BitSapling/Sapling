package com.github.bitsapling.sapling.controller.auth;

import cn.dev33.satoken.stp.StpUtil;
import com.github.bitsapling.sapling.entity.User;
import com.github.bitsapling.sapling.exception.APIErrorCode;
import com.github.bitsapling.sapling.exception.APIGenericException;
import com.github.bitsapling.sapling.service.UserService;
import com.github.bitsapling.sapling.util.IPUtil;
import com.github.bitsapling.sapling.util.PasswordHash;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

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

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginDto login) {
        if (login.getUser() == null) {
            throw new APIGenericException(MISSING_PARAMETERS, "User parameter is required");
        }
        if (login.getPassword() == null) {
            throw new APIGenericException(MISSING_PARAMETERS, "Password parameter is required");
        }
        User user = userService.getUserByUsername(login.getUser());
        if (user == null) userService.getUserByEmail(login.getUser());
        if (user == null) {
            log.warn("IP {} tried to login with not exists username {}", IPUtil.getRequestIp(request), login.getUser());
            throw new APIGenericException(AUTHENTICATION_FAILED);
        }
        if (!PasswordHash.verify(login.getPassword(), user.getPasswordHash())) {
            log.warn("IP {} tried to login with username {} and password {}", IPUtil.getRequestIp(request), login.getUser(), login.getPassword());
            throw new APIGenericException(AUTHENTICATION_FAILED, "Username or Password incorrect");
        }
        StpUtil.login(user.getId());
        Map<String, Object> loginResponse = new LinkedHashMap<>();
        //loginResponse.put("status", "ok");
        loginResponse.put("token", StpUtil.getTokenInfo());
        Map<String, Object> basicInformation = new LinkedHashMap<>();
        basicInformation.put("id", user.getId());
        basicInformation.put("role", user.getGroup().getCode());
        basicInformation.put("name", user.getUsername());
        basicInformation.put("email", user.getEmail());
        basicInformation.put("language", user.getLanguage());
        Map<String, Object> endpoints = new LinkedHashMap<>();
        endpoints.put("user.info", "/user/info");
        loginResponse.put("user", basicInformation);
        loginResponse.put("endpoints", endpoints);
        return loginResponse;
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

    @PostMapping("/status")
    public Map<String, Object> status() {
        Map<String, Object> resp = new LinkedHashMap<>();
        //resp.put("status", "ok");
        resp.put("isLogin", StpUtil.isLogin());
        resp.put("isSafe", StpUtil.isSafe());
        resp.put("isSwitch", StpUtil.isSwitch());
        return resp;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    static class LoginDto {
        private String user;
        private String password;
    }
}
