package com.github.bitsapling.sapling.service;

import com.github.bitsapling.sapling.entity.PromotionPolicy;
import com.github.bitsapling.sapling.repository.PromotionPolicyRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service

public class PromotionService {
    @Autowired
    private PromotionPolicyRepository repository;

    @Nullable
    public PromotionPolicy getPromotionPolicy(long id) {
        Optional<PromotionPolicy> entity = repository.findById(id);
        return entity.orElse(null);
    }

    @Nullable
    public PromotionPolicy getPromotionPolicy(@NotNull String name) {
        Optional<PromotionPolicy> entity = repository.findPromotionPolicyBySlug(name);
        return entity.orElse(null);
    }

    @Nullable
    public PromotionPolicy getDefaultPromotionPolicy() {
        return repository.findAll().iterator().next();
    }

    @NotNull
    public List<PromotionPolicy> getAllPromotionPolicies() {
        List<PromotionPolicy> policies = new ArrayList<>();
        repository.findAll().forEach(policies::add);
        return policies;
    }

    @NotNull
    public PromotionPolicy save(@NotNull PromotionPolicy promotionPolicy) {
        return repository.save(promotionPolicy);
    }
}
