package com.github.bitsapling.sapling.autoconfig;

import com.github.bitsapling.sapling.util.Argon2idPwdUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Argon2idConfig {
    @Bean
    public Argon2idPwdUtil argon2idPwdUtil() {
        return new Argon2idPwdUtil();
    }
}
