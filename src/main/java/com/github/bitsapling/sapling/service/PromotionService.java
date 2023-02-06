package com.github.bitsapling.sapling.service;

import com.github.bitsapling.sapling.entity.PromotionPolicyEntity;
import com.github.bitsapling.sapling.objects.PromotionPolicy;
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
        Optional<PromotionPolicyEntity> entity = repository.findById(id);
        return entity.map(this::convert).orElse(null);
    }

    @NotNull
    public PromotionPolicy convert(PromotionPolicyEntity entity) {
        return new PromotionPolicy(
                entity.getId(),
                entity.getDisplayName(),
                entity.getUploadRatio(),
                entity.getDownloadRatio()
        );
    }

    @NotNull
    public PromotionPolicyEntity convert(PromotionPolicy object) {
        return new PromotionPolicyEntity(
                object.getId(),
                object.getDisplayName(),
                object.getUploadRatio(),
                object.getDownloadRatio()
        );
    }

    public PromotionPolicy save(@NotNull PromotionPolicy promotionPolicy) {
        PromotionPolicyEntity entity = new PromotionPolicyEntity(
                promotionPolicy.getId(),
                promotionPolicy.getDisplayName(),
                promotionPolicy.getUploadRatio(),
                promotionPolicy.getDownloadRatio()
        );
       return convert(repository.save(entity));
    }
}
