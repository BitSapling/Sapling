package com.github.bitsapling.sapling.hook;

import com.github.bitsapling.sapling.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class PluginManagerStartupHook implements ApplicationListener<ApplicationStartedEvent> {
    @Autowired
    private PluginManager pluginManager;

    @Override
    public void onApplicationEvent(@NotNull ApplicationStartedEvent event) {
        pluginManager.enablePlugin();
    }
}
