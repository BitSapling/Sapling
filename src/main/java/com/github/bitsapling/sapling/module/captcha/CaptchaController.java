package com.github.bitsapling.sapling.module.captcha;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.github.bitsapling.sapling.cache.GlobalCache;
import com.github.bitsapling.sapling.controller.ApiCode;
import com.github.bitsapling.sapling.controller.ApiResponse;
import com.github.bitsapling.sapling.util.IPUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/captcha")
@Slf4j
public class CaptchaController {
    private final static Digester SHA1 = new Digester(DigestAlgorithm.SHA1);
    @Autowired
    private GlobalCache cache;
    @Autowired
    private CaptchaService service;

    @GetMapping(value = "/", consumes = "application/json", produces = "application/json")
    public ApiResponse<?> generateCaptcha(HttpServletRequest request) {
        String ipHash = SHA1.digestHex(IPUtil.getRequestIp(request));
        long count = 0L;
        Object counter = cache.get("captcha-ip-counter-" + ipHash);
        if (counter instanceof Long l) {
            count = l;
        }
        count++;
        if (count > 50) {
            return new ApiResponse<>(ApiCode.BAD_REQUEST.code(), "Too many requests");
        }
        cache.set("captcha-ip-counter-" + ipHash, count, 60 * 15);
        return new ApiResponse<>(service.generateCaptcha());
    }


}
