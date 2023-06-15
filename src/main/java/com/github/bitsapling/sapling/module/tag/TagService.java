package com.github.bitsapling.sapling.module.tag;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.bitsapling.sapling.module.common.CommonService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class TagService extends ServiceImpl<TagMapper, Tag> implements CommonService<Tag> {
    @Nullable
    public Tag getTag(@NotNull String key) {
        LambdaQueryWrapper<Tag> wrapper = Wrappers
                .lambdaQuery(Tag.class)
                .eq(Tag::getName, key);
        return baseMapper.selectOne(wrapper);
    }
}
