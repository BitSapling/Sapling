package com.github.bitsapling.sapling.module.user;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.github.bitsapling.sapling.controller.ApiCode;
import com.github.bitsapling.sapling.controller.ApiResponse;
import com.github.bitsapling.sapling.module.audit.Audit;
import com.github.bitsapling.sapling.module.audit.AuditService;
import com.github.bitsapling.sapling.module.captcha.CaptchaService;
import com.github.bitsapling.sapling.module.failedlogin.FailedLogin;
import com.github.bitsapling.sapling.module.failedlogin.FailedLoginService;
import com.github.bitsapling.sapling.module.failedlogin.LoginBanService;
import com.github.bitsapling.sapling.module.setting.SettingService;
import com.github.bitsapling.sapling.module.user.dto.AuthRequestDTO;
import com.github.bitsapling.sapling.module.user.dto.UserLevelSelfReadOnlyDTO;
import com.github.bitsapling.sapling.util.Argon2idPwdUtil;
import com.github.bitsapling.sapling.util.IPUtil;
import com.github.bitsapling.sapling.util.SafeUUID;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    private final static Digester SHA512 = new Digester(DigestAlgorithm.SHA512);
    @Autowired
    private Argon2idPwdUtil pwdUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private AuditService auditService;
    @Autowired
    private FailedLoginService failedLoginService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private LoginBanService loginBanService;
    @Autowired
    private CaptchaService captchaService;

    @PostMapping("/")
    public ApiResponse<?> authNormal(@RequestBody AuthRequestDTO authRequestDTO,
                                     HttpServletRequest request) {
        String ip = IPUtil.getRequestIp(request);
//        if (loginBanService.isBanned(ip, LocalDateTime.now())) {
//            return new ApiResponse<>(ApiCode.AUTHENTICATION_FAILED.code(), "You are reached maximum login attempts. Please try again later.");
//        }
        // verify captcha
        boolean captchaVerified = captchaService.verifyCaptcha(SafeUUID.fromString(authRequestDTO.getCaptchaId()), authRequestDTO.getCaptchaCode());
        if (!captchaVerified) {
            recordLoginFailed(authRequestDTO, null, request);
            log.debug("Incorrect captcha {} from {}", authRequestDTO.getCaptchaCode(), ip);
            return new ApiResponse<>(ApiCode.AUTHENTICATION_FAILED.code(), "Captcha incorrect or expired");
        }
        // verify username
        User user = userService.getUserByUsername(authRequestDTO.getIdentifier());
        if (user == null) userService.getUserByEmail(authRequestDTO.getIdentifier());
        if (user == null) {
            recordLoginFailed(authRequestDTO, null, request);
            log.debug("Incorrect identifier {} from {}", authRequestDTO.getIdentifier(), ip);
            return new ApiResponse<>(ApiCode.AUTHENTICATION_FAILED.code(), "Identifier or password incorrect");
        }
        // verify password
        String passwordHash = user.getPassword();
        boolean passwordVerified = pwdUtil.validate(passwordHash, authRequestDTO.getCredential());
        if (!passwordVerified) {
            recordLoginFailed(authRequestDTO, user.getId(), request);
            log.debug("Incorrect credentials {} from {}", authRequestDTO.getIdentifier(), ip);
            return new ApiResponse<>(ApiCode.AUTHENTICATION_FAILED.code(), "Identifier or password incorrect");
        }
        // set login session
        log.debug("User {} successfully logged in with IP {}.", user.getUsername(), ip);
        auditService.save(new Audit(0L, user.getId(), "user_login", LocalDateTime.now(), ip, request.getHeader("User-Agent"), null));
        StpUtil.login(user.getId());
        return new ApiResponse<>(StpUtil.getTokenInfo());
    }

    @PostMapping("/logout")
    public ApiResponse<?> logout() {
        if (!StpUtil.isLogin()) {
            return new ApiResponse<>(ApiCode.UNAUTHORIZED.code(), "You already logged out!");
        }
        StpUtil.logout();
        return ApiResponse.ok();
    }

    @GetMapping("/me")
    public ApiResponse<?> me() {
        if (!StpUtil.isLogin()) {
            return new ApiResponse<>(ApiCode.UNAUTHORIZED.code());
        }
        User user = userService.getUser(StpUtil.getLoginIdAsLong());
        if (user == null) {
            return new ApiResponse<>(ApiCode.BAD_REQUEST.code(), "Login session valid but account not exists, is it deleted?");
        }
        return new ApiResponse<>(ApiCode.OK.code(), "already logged in", new UserLevelSelfReadOnlyDTO(user));
    }


    private void recordLoginFailed(AuthRequestDTO authRequestDTO, @Nullable Long uid, HttpServletRequest request) {
        String ip = IPUtil.getRequestIp(request);
        if (uid == null) uid = 0L;
        failedLoginService.save(new FailedLogin(0L, uid, LocalDateTime.now(),
                authRequestDTO.getIdentifier(),
                SHA512.digestHex(authRequestDTO.getCredential()),
                ip,
                request.getHeader("User-Agent")));
        int maxAttempts = settingService.getSetting("security.max_login_attempts").getValueAsInteger(10);
        int banLength = settingService.getSetting("security.login_attempt_ban").getValueAsInteger(900);
        long attempts = failedLoginService.getFailedAttempts(ip);
        if (attempts >= maxAttempts) {
            //loginBanService.saveOrUpdate(new LoginBan(0L, ip, LocalDateTime.now().plusSeconds(banLength)));
            log.warn("IP address {} is banned for {} seconds due to too many failed login attempts.", ip, banLength);
        }
    }


}
