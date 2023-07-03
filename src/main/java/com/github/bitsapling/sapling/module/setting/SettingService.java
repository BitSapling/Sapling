package com.github.bitsapling.sapling.module.setting;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.bitsapling.sapling.cache.GlobalCache;
import com.github.bitsapling.sapling.module.common.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SettingService extends ServiceImpl<SettingMapper, Setting> implements CommonService<Setting> {
    @Autowired
    private GlobalCache cache;

    public Setting getSetting(@NotNull String key) {
        Setting setting = fetchRedisCacheSetting(key);
        if (setting != null) {
            return setting;
        }
        setting = baseMapper.selectOne(lambdaQuery().eq(Setting::getKey, key));
        if (!saveRedisCacheSetting(key, setting)) {
            log.warn("Failed to save setting to redis cache");
        }
        return setting;
    }

    private boolean saveRedisCacheSetting(@NotNull String key, @NotNull Setting setting) {
        return cache.set("setting-cache-" + key, setting, 60);
    }

    private Setting fetchRedisCacheSetting(@NotNull String key) {
        Object settingObject = cache.get("setting-cache-" + key);
        if (settingObject instanceof Setting setting) {
            return setting;
        }
        return null;
    }
}
