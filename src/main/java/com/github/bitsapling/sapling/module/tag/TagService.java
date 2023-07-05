package com.github.bitsapling.sapling.module.tag;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.github.bitsapling.sapling.module.common.CommonService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class TagService extends ServiceImpl<TagMapper, Tag> implements CommonService<Tag> {
    @Nullable
    public Tag getTag(@NotNull Object identifier) {
        return ChainWrappers.lambdaQueryChain(Tag.class)
                .eq(Tag::getId, identifier)
                .or(w -> w.eq(Tag::getName, identifier))
                .one();
    }
}
