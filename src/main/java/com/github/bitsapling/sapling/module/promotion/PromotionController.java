package com.github.bitsapling.sapling.module.promotion;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.github.bitsapling.sapling.controller.ApiResponse;
import com.github.bitsapling.sapling.module.promotion.dto.PromotionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/promotion")
@Slf4j
public class PromotionController {
    @Autowired
    private PromotionService service;

    @GetMapping("/")
    @SaCheckPermission("promotion:read")
    public ApiResponse<?> listPromotions() {
        return new ApiResponse<>(service.list().stream().map(p -> (PromotionDTO) p).toList());
    }

    @GetMapping("/{identifier}")
    @SaCheckPermission("promotion:read")
    public ApiResponse<?> queryPromotion(@PathVariable("identifier") String identifier) {
        Promotion promotion = service.getPromotion(identifier);
        if (promotion != null) {
            return new ApiResponse<>((PromotionDTO) promotion);
        } else {
            return ApiResponse.notFound();
        }
    }

    @PostMapping("/")
    @SaCheckPermission("promotion:write")
    public ApiResponse<Void> writePromotion(@RequestBody PromotionDTO promotionDTO) {
        if (!service.saveOrUpdate(promotionDTO)) {
            throw new IllegalStateException("Failed to write the promotion to database.");
        }
        return ApiResponse.ok();
    }

    @DeleteMapping("/{identifier}")
    @SaCheckPermission("promotion:write")
    public ApiResponse<Void> deletePromotion(@PathVariable("identifier") String identifier) {
        Promotion promotion = service.getPromotion(identifier);
        if (promotion == null) {
            return ApiResponse.notFound();
        }
        if (!service.removeById(promotion)) {
            throw new IllegalStateException("Failed to delete the promotion from database.");
        }
        // Create a default promotion
        if (service.count() == 0) {
            service.save(new Promotion(0L, "Fallback Default", "https://example.com/not-configured", BigDecimal.ZERO, BigDecimal.ZERO, true));
        }
        return ApiResponse.ok();
    }

}
