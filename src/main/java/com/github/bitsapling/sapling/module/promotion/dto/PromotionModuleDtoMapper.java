package com.github.bitsapling.sapling.module.promotion.dto;

import com.github.bitsapling.sapling.module.promotion.Promotion;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PromotionModuleDtoMapper {
    PromotionModuleDtoMapper INSTANCE = Mappers.getMapper(PromotionModuleDtoMapper.class);

    DeployedPromotion toDeployedObject(Promotion promotion);
}
