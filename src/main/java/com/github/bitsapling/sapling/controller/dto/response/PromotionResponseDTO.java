package com.github.bitsapling.sapling.controller.dto.response;

import com.github.bitsapling.sapling.entity.PromotionPolicy;
import com.github.bitsapling.sapling.objects.ResponsePojo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PromotionResponseDTO extends ResponsePojo {
    private long id;
    private String slug;
    private String displayName;
    private double uploadRatio;
    private double downloadRatio;

    public PromotionResponseDTO(PromotionPolicy policy){
        this.id = policy.getId();
        this.slug = policy.getSlug();
        this.displayName = policy.getDisplayName();
        this.uploadRatio = policy.getUploadRatio();
        this.downloadRatio = policy.getDownloadRatio();
    }
}
