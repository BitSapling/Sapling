package com.github.bitsapling.sapling.module.promotion.dto;

import com.github.bitsapling.sapling.module.promotion.Promotion;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Validated
public class PromotionDTO extends Promotion {
    public PromotionDTO(@PositiveOrZero Long id, @NotEmpty String name, @NotEmpty String iconUrl, @NotNull BigDecimal uploadMultiplier, @NotNull BigDecimal downloadMultiplier, @NotNull Boolean isDefault) {
        super(id, name, iconUrl, uploadMultiplier, downloadMultiplier, isDefault);
    }
}
