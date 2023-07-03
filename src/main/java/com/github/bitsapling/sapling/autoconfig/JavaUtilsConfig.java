package com.github.bitsapling.sapling.autoconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class JavaUtilsConfig {
    @Bean
    public Random getRandom() {
        return new Random();
    }

}
