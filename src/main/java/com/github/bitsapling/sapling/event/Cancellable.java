package com.github.bitsapling.sapling.event;

public interface Cancellable {
    boolean isCancelled();

    void setCancelled(boolean cancel);
}
