package com.github.bitsapling.sapling.module.setting;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class SettingServiceImpl extends ServiceImpl<SettingMapper, Setting> implements SettingService {
    @Nullable
    public Setting getSetting(@NotNull String key) {
        LambdaQueryWrapper<Setting> wrapper = Wrappers
                .lambdaQuery(Setting.class)
                .eq(Setting::getKey, key);
        return baseMapper.selectOne(wrapper);
    }
}
