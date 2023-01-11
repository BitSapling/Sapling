package com.github.bitsapling.sapling.job.announce;

import com.github.bitsapling.sapling.type.AnnounceEventType;
import org.jetbrains.annotations.NotNull;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class AnnounceJob {
    private final Deque<AnnounceTask> taskQueue = new ConcurrentLinkedDeque<>();
    public AnnounceJob(){

    }

    public void insertTask(@NotNull AnnounceTask announceTask) {
        taskQueue.offer(announceTask);
    }

    record AnnounceTask(
            @NotNull String ip, int port, @NotNull String infoHash, @NotNull String peerId,
            long uploaded, long downloaded, long left, @NotNull AnnounceEventType event,
            int numWant, String key, boolean compact, boolean noPeerId,
            int supportCrypto, int redundant
    ){

    }
}
