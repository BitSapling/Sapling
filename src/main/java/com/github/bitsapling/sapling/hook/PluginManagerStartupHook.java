package com.github.bitsapling.sapling.hook;

import com.github.bitsapling.sapling.plugin.PluginManager;
import com.github.bitsapling.sapling.plugin.java.SaplingPluginManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 插件管理器启动监听器
 */
@Component
public class PluginManagerStartupHook implements ApplicationListener<ApplicationStartedEvent> {
    @Autowired
    private PluginManager pluginManager;

    /**
     * 应用程序已启动
     *
     * @param event 应用程序已启动事件
     */
    @Override
    public void onApplicationEvent(@NotNull ApplicationStartedEvent event) {
        SaplingPluginManager saplingPluginManager = (SaplingPluginManager) pluginManager;
        saplingPluginManager.enablePlugin();
        saplingPluginManager.setLoading(false);
    }
}
