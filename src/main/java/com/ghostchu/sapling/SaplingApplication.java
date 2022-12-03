package com.ghostchu.sapling;

import com.ghostchu.sapling.language.ITranslation;
import com.ghostchu.sapling.util.Translation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@Configuration
@EnableCaching
@EnableAsync
@EnableTransactionManagement
public class SaplingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SaplingApplication.class, args);
    }

    @Bean
    public ITranslation translation() {
        return new Translation();
    }
}
