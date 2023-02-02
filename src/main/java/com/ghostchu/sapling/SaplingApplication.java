package com.ghostchu.sapling;

import cn.dev33.satoken.SaManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@Configuration
@EnableCaching
@EnableAsync
@EnableTransactionManagement
@Slf4j
public class SaplingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SaplingApplication.class, args);
        log.info("Started Application with SA-Token configuration: {}", SaManager.getConfig());
    }
}
