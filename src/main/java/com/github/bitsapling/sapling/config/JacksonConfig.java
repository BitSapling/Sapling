package com.github.bitsapling.sapling.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {
    @Autowired
    Jackson2ObjectMapperBuilder mapperBuilder;
//    @Bean
//    @Primary
//    public ObjectMapper objectMapper() {
//        return mapperBuilder.build()
//                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
//                .registerModule(new JavaTimeModule());
//    }

    @Bean
    @Primary
    public ObjectMapper primaryObjectMapper() {
        return JsonMapper.builder().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .addModule(new JavaTimeModule()).build();
    }
}
