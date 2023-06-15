package com.github.bitsapling.sapling.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class ClassUtil {
    private final Cache<Class<?>, String> simpleNameCache = CacheBuilder.newBuilder()
            .maximumSize(4096)
            .build();

    @SneakyThrows(ExecutionException.class)
    public String getClassSimpleName(Class<?> clazz) {
        return simpleNameCache.get(clazz, clazz::getSimpleName);
    }

    public boolean isClassExists(@Nullable String className) {
        if (className == null) return false;
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
