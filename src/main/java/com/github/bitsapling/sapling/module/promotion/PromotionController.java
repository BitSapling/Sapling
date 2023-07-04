package com.github.bitsapling.sapling.module.promotion;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.github.bitsapling.sapling.controller.ApiResponse;
import com.github.bitsapling.sapling.module.promotion.dto.DeployedPromotion;
import com.github.bitsapling.sapling.module.promotion.dto.DraftedPromotion;
import com.github.bitsapling.sapling.module.promotion.dto.PromotionModuleDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/promotion")
@Slf4j
@Tag(name = "种子促销规则")
public class PromotionController {
    private static final PromotionModuleDtoMapper DTO_MAPPER = PromotionModuleDtoMapper.INSTANCE;
    @Autowired
    private PromotionService service;

    @Operation(summary = "列出所有促销规则")
    @GetMapping(value = "/", consumes = "application/json", produces = "application/json")
    @SaCheckPermission("promotion:read")
    public ApiResponse<List<DeployedPromotion>> listPromotions() {
        return new ApiResponse<>(service.list().stream().map(DTO_MAPPER::toDeployedObject).toList());
    }

    @Operation(summary = "查询指定促销规则详细信息")
    @GetMapping(value = "/{identifier}", consumes = "application/json", produces = "application/json")
    @SaCheckPermission("promotion:read")
    public ApiResponse<?> queryPromotion(@PathVariable("identifier") String identifier) {
        Promotion promotion = service.getPromotion(identifier);
        if (promotion != null) {
            return new ApiResponse<>(DTO_MAPPER.toDeployedObject(promotion));
        } else {
            return ApiResponse.notFound();
        }
    }

    @Operation(summary = "创建新的促销规则")
    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    @SaCheckPermission("promotion:write")
    public ApiResponse<Void> writePromotion(@RequestBody DraftedPromotion draftedPromotion) {
        Promotion promotion = new Promotion(0L, draftedPromotion.getName(), draftedPromotion.getIconUrl(), draftedPromotion.getUploadMultiplier(), draftedPromotion.getDownloadMultiplier(), draftedPromotion.getIsDefault());
        if (!service.save(promotion)) {
            throw new IllegalStateException("无法将促销规则保存到数据库中");
        }
        return ApiResponse.ok();
    }

    @Operation(summary = "更新指定促销规则")
    @PutMapping(value = "/{identifier}", consumes = "application/json", produces = "application/json")
    @SaCheckPermission("promotion:write")
    public ApiResponse<Void> updatePromotion(@PathVariable("identifier") String identifier, @RequestBody DraftedPromotion draftedPromotion) {
        Promotion promotion = service.getPromotion(identifier);
        if (promotion == null) {
            return ApiResponse.notFound();
        }
        promotion.setName(draftedPromotion.getName());
        promotion.setIconUrl(draftedPromotion.getIconUrl());
        promotion.setUploadMultiplier(draftedPromotion.getUploadMultiplier());
        promotion.setDownloadMultiplier(draftedPromotion.getDownloadMultiplier());
        promotion.setIsDefault(draftedPromotion.getIsDefault());
        if (!service.updateById(promotion)) {
            throw new IllegalStateException("无法将促销规则更新到数据库中");
        }
        return ApiResponse.ok();
    }


    @DeleteMapping(value = "/{identifier}", consumes = "application/json", produces = "application/json")
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
