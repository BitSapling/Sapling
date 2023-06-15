package com.github.bitsapling.sapling.util;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;

public class PerfMonitorPlus implements AutoCloseable {
    private final Logger LOGGER = LoggerFactory.getLogger(PerfMonitorPlus.class);
    private final Instant monitorStartedAt = Instant.now();
    private final String monitorName;
    private final Deque<String> stages = new ConcurrentLinkedDeque<>();
    private final Deque<Instant> stageTimers = new ConcurrentLinkedDeque<>();
    private final Map<UUID, PerfEntry> history = new ConcurrentHashMap<>();
    private final List<UUID> treeMap = new CopyOnWriteArrayList<>();

    public PerfMonitorPlus(@NotNull String name) {
        this.monitorName = name;
    }


    public String getCurrentStage() {
        return stages.peek();
    }

    @NotNull
    public PerfMonitorPlus.Child child(@NotNull String newStageName) {
        UUID uuid = UUID.randomUUID();
        treeMap.add(uuid);
        return new Child(this, newStageName, uuid);
    }

    public Instant getMonitorStartedAt() {
        return monitorStartedAt;
    }


    public String getMonitorName() {
        return monitorName;
    }


    @Override
    public void close() {
        String report = generateReport();
        LOGGER.atLevel(Level.DEBUG).log(report);
    }

    @NotNull
    public String generateReport() {
        StringJoiner joiner = new StringJoiner("\n");
        long totalCostMs = monitorStartedAt.until(Instant.now(), ChronoUnit.MILLIS);
        joiner.add("Performance summary for " + monitorName + " monitor: ");
        joiner.add("Root - " + totalCostMs + "ms");
        for (UUID uuid : treeMap) {
            PerfEntry entry = history.get(uuid);
            if (entry == null) {
                joiner.add("#### ChildTask didn't closed before generate report ####");
            } else {
                joiner.add(entry.output());
            }
        }
        joiner.add("Root - " + totalCostMs + "ms");
        return joiner.toString();
    }

    public static class Child implements AutoCloseable {
        private final PerfMonitorPlus parent;
        private final UUID uuid;

        public Child(@NotNull PerfMonitorPlus parent, @NotNull String stageName, @NotNull UUID uuid) {
            this.parent = parent;
            this.parent.stages.push(stageName);
            this.parent.stageTimers.push(Instant.now());
            this.uuid = uuid;
        }

        @Override
        public void close() {
            if (this.parent.stages.size() == 0 || this.parent.stageTimers.size() == 0) {
                this.parent.LOGGER.warn("PerfMonitor failed to quit the stage because there no more stage are able to quit! (Is enter not paired with quit?)");
                return;
            }
            String stageName = this.parent.stages.pop();
            Instant stageStartAt = this.parent.stageTimers.pop();
            Instant stageEndAt = Instant.now();
            long costMs = stageStartAt.until(stageEndAt, ChronoUnit.MILLIS);
            this.parent.history.put(uuid, new PerfEntry(stageName, stageStartAt, stageEndAt, costMs, this.parent.stages.size()));
        }
    }

    @Data
    private static class PerfEntry {
        private String stage;
        private Instant startedAt;
        private Instant endedAt;
        private long costMs;
        private int depth;

        public PerfEntry(String stage, Instant startedAt, Instant endedAt, long costMs, int depth) {
            this.stage = stage;
            this.startedAt = startedAt;
            this.endedAt = endedAt;
            this.costMs = costMs;
            this.depth = depth;
        }

        public String output() {
            return "  " + " ".repeat((depth * 2)) + stage + " - " + costMs + " ms";
        }

    }
}
