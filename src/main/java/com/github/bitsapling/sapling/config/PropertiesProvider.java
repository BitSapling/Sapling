package com.github.bitsapling.sapling.config;

import com.github.bitsapling.sapling.SaplingApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class PropertiesProvider {

    @Bean
    public void getProperties() {
        String fileName = "application.yml";
        File file = new File(fileName);
        Properties properties = new Properties();
        try {
            InputStream in = new FileInputStream(file);
            properties.load(in);
            log.info("Loading configuration from {}", fileName);
        } catch (IOException e) {
            try {
                InputStream in = SaplingApplication.class.getClassLoader().getResourceAsStream(fileName);
                properties.load(in);
            } catch (IOException es) {
                System.out.println(es.getMessage());
            }
        }
    }
}
