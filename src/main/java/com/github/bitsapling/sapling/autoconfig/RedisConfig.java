package com.github.bitsapling.sapling.autoconfig;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
@EnableRedisRepositories
public class RedisConfig {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheConfiguration = redisCacheConfiguration
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer.UTF_8))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer()))
                .prefixCacheNameWith("sapling:")
                .entryTtl(Duration.ofMinutes(3L));
        return RedisCacheManager
                .builder(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                //.withInitialCacheConfigurations(map)
                .build();
    }

    @Bean
    public RedisConnectionFactory connectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName()).append(":");
            sb.append(method.getName()).append(":");
            Object key = SimpleKeyGenerator.generateKey(params);
            return sb.append(key);
        };
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = serializer();
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(StringRedisSerializer.UTF_8);
        redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(StringRedisSerializer.UTF_8);
        redisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    public GenericJackson2JsonRedisSerializer serializer() {
        return new GenericJackson2JsonRedisSerializer();
    }
}
