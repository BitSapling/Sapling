package com.github.bitsapling.sapling.service;

import com.github.bitsapling.sapling.entity.Tag;
import com.github.bitsapling.sapling.repository.TagRepository;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@Transactional
public class TagService {
    @Autowired
    private TagRepository repository;

    @Nullable
    public Tag getTag(@NotNull String tagName) {
        tagName = tagName.toLowerCase(Locale.ROOT);
        return repository.findByName(tagName).orElse(null);
    }
    @Nullable
    public Tag getTag(long id){
        return repository.findById(id).orElse(null);
    }

    @NotNull
    public Tag save(@NotNull Tag tag){
        return repository.save(tag);
    }

    @NotNull
    public Iterable<Tag> getAllTags(){
        return repository.findAll();
    }
}
