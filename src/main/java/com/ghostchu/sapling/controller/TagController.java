package com.ghostchu.sapling.controller;

import com.ghostchu.sapling.domain.model.Tag;
import com.ghostchu.sapling.repository.TagRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CacheConfig(cacheNames = "api_tags")
@RequestMapping("/tag")
public class TagController {
    private final TagRepository repository;

    public TagController(@Autowired TagRepository repository) {
        this.repository = repository;
    }

    @NotNull
    @Cacheable(value = "list")
    @GetMapping("/list")
    public List<PublicTag> getAllTags() {
        return repository.listAllTags().stream().map(PublicTag::new).toList();
    }

    @Nullable
    @Cacheable(value = "query")
    @GetMapping("/query")
    public Tag getTagByName(@RequestParam String name) {
        if (name == null) {
            return null;
        }
        return repository.findByNameEqualsIgnoreCase(name).orElse(null);
    }

    @Nullable
    @Cacheable(value = "search")
    @GetMapping("/search")
    public List<Tag> searchTagByName(@RequestParam String name) {
        if (name == null) {
            return null;
        }
        return repository.findByNameContainsIgnoreCase(name);
    }


    static class PublicTag {
        private final long id;
        private final String name;
        private final String color;

        public PublicTag(@NotNull Tag tag) {
            this.id = tag.getTagId();
            this.name = tag.getName();
            this.color = tag.getHexColor();
        }

        public long getId() {
            return id;
        }

        public @NotNull String getName() {
            return name;
        }

        @NotNull
        public String getColor() {
            return color;
        }

    }
}
