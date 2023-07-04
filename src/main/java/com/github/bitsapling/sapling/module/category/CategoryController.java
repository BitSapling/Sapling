package com.github.bitsapling.sapling.module.category;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.github.bitsapling.sapling.controller.ApiResponse;
import com.github.bitsapling.sapling.module.category.dto.CategoryModuleMapper;
import com.github.bitsapling.sapling.module.category.dto.DraftedCategory;
import com.github.bitsapling.sapling.module.category.dto.PublishedCategory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
@Tag(name = "种子分类")
public class CategoryController {
    private static final CategoryModuleMapper DTO_MAPPER = CategoryModuleMapper.INSTANCE;
    @Autowired
    private CategoryService service;

    @Operation(summary = "列出所有种子分类")
    @GetMapping(value = "/", consumes = "application/json", produces = "application/json")
    @SaCheckPermission("category:read")
    public ApiResponse<List<PublishedCategory>> listCategories() {
        return new ApiResponse<>(service.list().stream().map(DTO_MAPPER::toPublishedObject).toList());
    }

    @Operation(summary = "读取指定种子分类")
    @GetMapping(value = "/{identifier}", consumes = "application/json", produces = "application/json")
    @SaCheckPermission("category:read")
    public ApiResponse<?> queryCategory(@PathVariable("identifier") String identifier) {
        Category category = service.getCategory(identifier);
        if (category != null) {
            return new ApiResponse<>(DTO_MAPPER.toPublishedObject(category));
        } else {
            return ApiResponse.notFound();
        }
    }

    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    @SaCheckPermission("category:write")
    @Operation(summary = "创建一个新的种子分类")
    public ApiResponse<Void> writeCategory(@Valid @RequestBody DraftedCategory draftedCategory) {
        Category category = new Category(0L,
                draftedCategory.getName(),
                draftedCategory.getIconUrl(),
                draftedCategory.getCssClassName(),
                draftedCategory.getPermissionName());
        if (!service.save(category)) {
            throw new IllegalStateException("Failed to write the category to database.");
        }
        return ApiResponse.ok();
    }

    @PutMapping(value = "/{identifier}", consumes = "application/json", produces = "application/json")
    @SaCheckPermission("category:write")
    @Operation(summary = "更新指定的种子分类")
    public ApiResponse<Void> updateCategory(@PathVariable("identifier") String identifier, @Valid @RequestBody DraftedCategory draftedCategory) {
        Category category = service.getCategory(identifier);
        if (category == null) {
            return ApiResponse.notFound();
        }
        if (!service.updateById(category)) {
            throw new IllegalStateException("Failed to update the category to database.");
        }
        return ApiResponse.ok();
    }


}
