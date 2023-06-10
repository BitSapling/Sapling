package com.github.bitsapling.sapling.util;

import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ExecutorUtil {
    private final ExecutorService announceExecutor = Executors.newWorkStealingPool();

    public ExecutorService getAnnounceExecutor() {
        return announceExecutor;
    }
}
