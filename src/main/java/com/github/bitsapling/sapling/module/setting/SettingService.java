package com.github.bitsapling.sapling.module.setting;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.bitsapling.sapling.module.promotion.Promotion;
import com.github.bitsapling.sapling.module.promotion.PromotionMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class SettingService {
    @Autowired
    private SettingMapper mapper;

    @NotNull
    public List<Setting> getAllSettings() {
        return mapper.selectList(Wrappers.lambdaQuery(Setting.class));
    }

    @Nullable
    public Setting getSetting(@NotNull Long id) {
        return mapper.selectById(id);
    }

    @Nullable
    public Setting getSetting(@NotNull String key) {
        LambdaQueryWrapper<Setting> wrapper = Wrappers
                .lambdaQuery(Setting.class)
                .eq(Setting::getKey, key);
        return mapper.selectOne(wrapper);
    }

    public int addSetting(@NotNull Setting setting) {
        return mapper.insert(setting);
    }

    public int deleteSetting(@NotNull Setting setting) {
        return mapper.deleteById(setting.getId());
    }

    public int deleteSetting(@NotNull Long id) {
        return mapper.deleteById(id);
    }

    public int updateSetting(@NotNull Setting setting) {
        return mapper.updateById(setting);
    }
}
