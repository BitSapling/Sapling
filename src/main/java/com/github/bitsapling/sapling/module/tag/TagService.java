package com.github.bitsapling.sapling.module.tag;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    @Autowired
    private TagMapper mapper;

    @NotNull
    public List<Tag> getAllTags() {
        return mapper.selectList(Wrappers.lambdaQuery(Tag.class));
    }

    @Nullable
    public Tag getTag(@NotNull Long id) {
        return mapper.selectById(id);
    }

    @Nullable
    public Tag getTag(@NotNull String key) {
        LambdaQueryWrapper<Tag> wrapper = Wrappers
                .lambdaQuery(Tag.class)
                .eq(Tag::getName, key);
        return mapper.selectOne(wrapper);
    }

    public int addTag(@NotNull Tag tag) {
        return mapper.insert(tag);
    }

    public int deleteTag(@NotNull Tag tag) {
        return mapper.deleteById(tag.getId());
    }

}
