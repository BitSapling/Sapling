package com.github.bitsapling.sapling.module.promotion.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode
@Data
@Valid
public class DraftedPromotion {
    @NotEmpty
    private String name;
    @NotEmpty
    private String iconUrl;
    @NotNull
    private BigDecimal uploadMultiplier;
    @NotNull
    private BigDecimal downloadMultiplier;
    @NotNull
    private Boolean isDefault;
}
