package com.github.bitsapling.sapling.module.tag;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.github.bitsapling.sapling.controller.ApiResponse;
import com.github.bitsapling.sapling.module.tag.dto.DraftedTag;
import com.github.bitsapling.sapling.module.tag.dto.RegisteredTag;
import com.github.bitsapling.sapling.module.tag.dto.TagModuleDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag")
@Slf4j
@Valid
@io.swagger.v3.oas.annotations.tags.Tag(name = "种子标签")
public class TagController {
    private static final TagModuleDtoMapper DTO_MAPPER = TagModuleDtoMapper.INSTANCE;
    @Autowired
    private TagService service;

    @Operation(summary = "列出所有标签")
    @GetMapping("/")
    @SaCheckPermission("tag:read")
    public ApiResponse<List<RegisteredTag>> listTags() {
        return new ApiResponse<>(service.list().stream().map(DTO_MAPPER::toRegisteredObject).toList());
    }

    @Operation(summary = "查询指定标签")
    @GetMapping("/{identifier}")
    @SaCheckPermission("tag:read")
    public ApiResponse<?> queryTag(@PathVariable("identifier") String identifier) {
        Tag tag = service.getTag(identifier);
        if (tag != null) {
            return new ApiResponse<>(DTO_MAPPER.toRegisteredObject(tag));
        } else {
            return ApiResponse.notFound();
        }
    }

    @Operation(summary = "创建新标签")
    @PostMapping("/")
    @SaCheckPermission("tag:write")
    public ApiResponse<Void> writeTag(@RequestBody DraftedTag draftedTag) {
        Tag tag = new Tag(0L, draftedTag.getName());
        if (!service.save(tag)) {
            throw new IllegalStateException("无法保存新的种子标签到数据库中");
        }
        return ApiResponse.ok();
    }

    @Operation(summary = "更新指定标签")
    @PutMapping("/{identifier}")
    @SaCheckPermission("tag:write")
    public ApiResponse<Void> updateTag(@PathVariable("identifier") String identifier, @RequestBody DraftedTag draftedTag) {
        Tag tag = service.getTag(identifier);
        if (tag == null) {
            return ApiResponse.notFound();
        }
        tag.setName(draftedTag.getName());
        if (!service.updateById(tag)) {
            throw new IllegalStateException("无法更新种子标签到数据库中");
        }
        return ApiResponse.ok();
    }

    @Operation(summary = "删除指定标签")
    @DeleteMapping("/{identifier}")
    @SaCheckPermission("tag:write")
    public ApiResponse<Void> deleteTag(@PathVariable("identifier") String identifier) {
        Tag tag = service.getTag(identifier);
        if (tag == null) {
            return ApiResponse.notFound();
        }
        if (!service.removeById(tag)) {
            throw new IllegalStateException("Failed to delete the tag from database.");
        }
        return ApiResponse.ok();
    }
}
