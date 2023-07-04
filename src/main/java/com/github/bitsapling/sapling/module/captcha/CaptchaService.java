package com.github.bitsapling.sapling.module.captcha;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import com.github.bitsapling.sapling.cache.GlobalCache;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CaptchaService {
    @Autowired
    private GlobalCache cache;

    public boolean verifyCaptcha(UUID id, String code) {
        if (id == null || code == null) return false;
        Object correctCodeObject = cache.get("captcha-ask-" + id);
        if (!(correctCodeObject instanceof String correctCode)) {
            return false;
        }
        return correctCode.equalsIgnoreCase(code);
    }

    public void invalidCaptcha(UUID id) {
        cache.del("captcha-ask-" + id);
    }

    public PublicCaptcha generateCaptcha() {
        GeneratedCaptcha generatedCaptcha = _generate();
        cache.set("captcha-ask-" + generatedCaptcha.getId(), generatedCaptcha.getCode(), 60 * 15);
        return new PublicCaptcha(generatedCaptcha.getId(), generatedCaptcha.getImage());
    }

    private GeneratedCaptcha _generate() {
        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(200, 45, 4, 4);
        captcha.setGenerator(new RandomGenerator(4));
        captcha.createCode();
        return new GeneratedCaptcha(UUID.randomUUID(), captcha.getImageBase64Data(), captcha.getCode());
    }

    @AllArgsConstructor
    @Data
    static class PublicCaptcha {
        private UUID id;
        private String image;
    }

    @AllArgsConstructor
    @Data
    static class GeneratedCaptcha {
        private UUID id;
        private String image;
        private String code;
    }
}
