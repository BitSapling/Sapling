package com.github.bitsapling.sapling.objects;

import com.github.bitsapling.sapling.entity.PromotionPolicyEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SeedBox {
    private final long id;
    private String address;
    private PromotionPolicyEntity downloadMultiplier;
    private PromotionPolicyEntity uploadMultiplier;
}
