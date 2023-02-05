package com.github.bitsapling.sapling.objects;

import com.github.bitsapling.sapling.entity.PromotionPolicyEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@AllArgsConstructor
@Data
public class SeedBox implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final long id;
    private String address;
    private PromotionPolicyEntity downloadMultiplier;
    private PromotionPolicyEntity uploadMultiplier;
}
