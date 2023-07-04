package com.github.bitsapling.sapling.module.promotion.dto;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode
@Data
@Valid
public class DeployedPromotion {
    private Long id;
    private String name;
    private String iconUrl;
    private BigDecimal uploadMultiplier;
    private BigDecimal downloadMultiplier;
    private Boolean isDefault;
}
