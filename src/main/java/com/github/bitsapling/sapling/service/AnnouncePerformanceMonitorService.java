package com.github.bitsapling.sapling.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.UUID;

public class AnnouncePerformanceMonitorService {
    private long handled = 0;
    private Cache<UUID, Long> announceTimes = CacheBuilder
            .newBuilder()
            .maximumSize(1000)
            .build();
    public void recordStats(long ns){
        announceTimes.put(UUID.randomUUID(), ns);
    }

    public double avgNs(){
        return announceTimes.asMap().values().stream().mapToLong(Long::longValue).average().orElse(0);
    }
    public double avgMs(){
        return avgNs() / 1000000;
    }
}
