package com.github.bitsapling.sapling.controller.dto.response;

import com.github.bitsapling.sapling.entity.PromotionPolicy;
import lombok.Getter;

@Getter
public class PromotionResponseDTO {
    private final long id;
    private final String slug;
    private final String displayName;
    private final double uploadRatio;
    private final double downloadRatio;

    public PromotionResponseDTO(PromotionPolicy policy){
        this.id = policy.getId();
        this.slug = policy.getSlug();
        this.displayName = policy.getDisplayName();
        this.uploadRatio = policy.getUploadRatio();
        this.downloadRatio = policy.getDownloadRatio();
    }
}
