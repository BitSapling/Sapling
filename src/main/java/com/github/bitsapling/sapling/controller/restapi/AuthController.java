package com.github.bitsapling.sapling.controller.restapi;

import cn.dev33.satoken.stp.StpUtil;
import com.github.bitsapling.sapling.exception.LoginException;
import com.github.bitsapling.sapling.objects.User;
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

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginDto login) throws LoginException {

        if (login.getUser() == null) {
            throw new IllegalArgumentException("User parameter is required");
        }
        if (login.getPassword() == null) {
            throw new IllegalArgumentException("Password parameter is required");
        }
        User user = userService.getUserByUsername(login.getUser());
        if (user == null) userService.getUserByEmail(login.getUser());
        if (user == null) {
            log.warn("IP {} tried to login with not exists username {}", IPUtil.getRequestIp(request), login.getUser());
            throw new LoginException("Username or Password incorrect");
        }
        if (!PasswordHash.verify(login.getPassword(), user.getPasswordHash())) {
            log.warn("IP {} tried to login with username {} and password {}", IPUtil.getRequestIp(request), login.getUser(), login.getPassword());
            throw new LoginException("Username or Password incorrect");
        }
        StpUtil.login(user.getId());
        Map<String, Object> loginResponse = new LinkedHashMap<>();
        loginResponse.put("status", "ok");
        Map<String, Object> basicInformation = new LinkedHashMap<>();
        basicInformation.put("user_id", user.getId());
        basicInformation.put("user_role", user.getGroup().getCode());
        basicInformation.put("user_name", user.getUsername());
        basicInformation.put("user_email", user.getEmail());
        basicInformation.put("user_language", user.getLanguage());
        Map<String, Object> endpoints = new LinkedHashMap<>();
        endpoints.put("user.info", "/user/info");
        loginResponse.put("user", basicInformation);
        loginResponse.put("endpoints", endpoints);
        return loginResponse;
    }

    @PostMapping("/logout")
    public Map<String, Object> logout() throws LoginException {
        if (StpUtil.isLogin()) {
            StpUtil.logout();
            Map<String, Object> logoutResponse = new LinkedHashMap<>();
            logoutResponse.put("status", "ok");
            return logoutResponse;
        } else {
            throw new LoginException("You are not logged in yet");
        }
    }

    @PostMapping("/status")
    public Map<String, Object> status() {
        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("status", "ok");
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
