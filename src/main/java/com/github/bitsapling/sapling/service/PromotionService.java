package com.github.bitsapling.sapling.service;

import com.github.bitsapling.sapling.entity.PromotionPolicy;
import com.github.bitsapling.sapling.repository.PromotionPolicyRepository;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class PromotionService {
    @Autowired
    private PromotionPolicyRepository repository;

    @Nullable
    public PromotionPolicy getPromotionPolicy(long id) {
        Optional<PromotionPolicy> entity = repository.findById(id);
        return entity.orElse(null);
    }

    @Nullable
    public PromotionPolicy getDefaultPromotionPolicy() {
        return repository.findAll().iterator().next();
    }

    @NotNull
    public PromotionPolicy save(@NotNull PromotionPolicy promotionPolicy) {
        return repository.save(promotionPolicy);
    }
}
