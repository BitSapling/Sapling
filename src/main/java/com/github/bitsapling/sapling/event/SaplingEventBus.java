package com.github.bitsapling.sapling.event;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class SaplingEventBus {
    private final EventBus saplingEventBus = EventBus.builder().build();

    @NotNull
    protected EventBus getSaplingEventBus() {
        return saplingEventBus;
    }

    public void callEvent(@NotNull Event event) {
        saplingEventBus.post(event);
    }

    public void registerListener(@NotNull Listener listener) {
        saplingEventBus.register(listener);
    }

    public void unregisterListener(@NotNull Listener listener) {
        saplingEventBus.unregister(listener);
    }

    public boolean isRegistered(@NotNull Listener listener) {
        return saplingEventBus.isRegistered(listener);
    }

    public void cancelEventDelivery(@NotNull Event event) {
        saplingEventBus.cancelEventDelivery(event);
    }

    public boolean hasSubscriberForEvent(@NotNull Class<? extends Event> eventClass) {
        return saplingEventBus.hasSubscriberForEvent(eventClass);
    }
}
