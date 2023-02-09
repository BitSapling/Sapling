package com.github.bitsapling.sapling.controller.category;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.github.bitsapling.sapling.controller.dto.response.CategoryResponseDTO;
import com.github.bitsapling.sapling.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    @SaCheckPermission("category:list")
    public List<CategoryResponseDTO> listCategory(){
        return categoryService.getAllCategories().stream().map(CategoryResponseDTO::new).toList();
    }
}
