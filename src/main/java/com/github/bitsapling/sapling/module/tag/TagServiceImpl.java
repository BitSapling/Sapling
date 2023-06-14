package com.github.bitsapling.sapling.module.tag;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    @Nullable
    public Tag getTag(@NotNull String key) {
        LambdaQueryWrapper<Tag> wrapper = Wrappers
                .lambdaQuery(Tag.class)
                .eq(Tag::getName, key);
        return baseMapper.selectOne(wrapper);
    }
}
