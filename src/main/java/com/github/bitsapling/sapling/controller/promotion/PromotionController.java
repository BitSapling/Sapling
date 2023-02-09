package com.github.bitsapling.sapling.controller.promotion;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.github.bitsapling.sapling.controller.dto.response.PromotionResponseDTO;
import com.github.bitsapling.sapling.service.PromotionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/promotion")
@Slf4j
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    @GetMapping("/list")
    @SaCheckPermission("promotion:list")
    public List<PromotionResponseDTO> listPromotions(){
        return promotionService.getAllPromotionPolicies().stream().map(PromotionResponseDTO::new).toList();
    }

}
