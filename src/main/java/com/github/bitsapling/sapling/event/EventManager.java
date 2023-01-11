package com.github.bitsapling.sapling.event;

import com.google.common.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class EventManager {
    private final EventBus BUS = new EventBus("main");

    public void post(@NotNull Event event) {
        BUS.post(event);
    }

    public void register(@NotNull Object listener) {
        BUS.register(listener);
    }

    public void unregister(@NotNull Object listener) {
        BUS.unregister(listener);
    }
}
