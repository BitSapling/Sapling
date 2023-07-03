package com.github.bitsapling.sapling.module.user.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
public class AuthRequestDTO {
    private String identifier;
    private String credential;
    private String captchaId;
    private String captchaCode;

    public AuthRequestDTO(@NotEmpty String identifier, @NotEmpty String credential, @NotEmpty String captchaId, @NotEmpty String captchaCode) {
        this.identifier = identifier;
        this.credential = credential;
        this.captchaId = captchaId;
        this.captchaCode = captchaCode;
    }
}
