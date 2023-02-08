package com.github.bitsapling.sapling.controller.auth;

import cn.dev33.satoken.stp.StpUtil;
import com.github.bitsapling.sapling.entity.User;
import com.github.bitsapling.sapling.exception.APIErrorCode;
import com.github.bitsapling.sapling.exception.APIGenericException;
import com.github.bitsapling.sapling.service.UserGroupService;
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
    public Map<String, Object> login(@RequestBody LoginDto login) {
        if (login.getUser() == null) {
            throw new APIGenericException(MISSING_PARAMETERS, "User parameter is required");
        }
        if (login.getPassword() == null) {
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

    @PostMapping("/status")
    public Map<String, Object> status() {
        Map<String, Object> resp = new LinkedHashMap<>();
        //resp.put("status", "ok");
        resp.put("isLogin", StpUtil.isLogin());
        resp.put("isSafe", StpUtil.isSafe());
        resp.put("isSwitch", StpUtil.isSwitch());
        return resp;
    }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody RegisterDto register) {
        if(register.getEmail() == null){
            throw new APIGenericException(MISSING_PARAMETERS, "Email parameter is required");
        }
        if(register.getUsername() == null){
            throw new APIGenericException(MISSING_PARAMETERS, "Username parameter is required");
        }
        if(register.getPassword() == null){
            throw new APIGenericException(MISSING_PARAMETERS, "Password parameter is required");
        }
        User user = userService.getUserByUsername(register.getUsername());
        if(user != null){
            throw new APIGenericException(APIErrorCode.USERNAME_ALREADY_IN_USAGE);
        }
        user = userService.getUserByEmail(register.getEmail());
        if(user != null){
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
                0L,0L,0L,0L,
                "未知",
                BigDecimal.ZERO,
                0,
                0L,
                UUID.randomUUID().toString()));
        StpUtil.login(user.getId());
        return getUserBasicInformation(user);
    }

    private Map<String, Object> getUserBasicInformation(User user){
        Map<String, Object> response = new LinkedHashMap<>();
        //loginResponse.put("status", "ok");
        response.put("token", StpUtil.getTokenInfo());
        Map<String, Object> basicInformation = new LinkedHashMap<>();
        basicInformation.put("id", user.getId());
        basicInformation.put("role", user.getGroup().getCode());
        basicInformation.put("name", user.getUsername());
        basicInformation.put("email", user.getEmail());
        basicInformation.put("language", user.getLanguage());
        response.put("user", basicInformation);
        return response;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    static class LoginDto {
        private String user;
        private String password;
    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    private static class RegisterDto {
        private String username;
        private String password;
        private String email;

    }
}
