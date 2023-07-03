package com.github.bitsapling.sapling.module.tag;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.github.bitsapling.sapling.controller.ApiResponse;
import com.github.bitsapling.sapling.module.tag.dto.TagDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag")
@Slf4j
public class TagController {
    @Autowired
    private TagService service;

    @GetMapping("/")
    @SaCheckPermission("tag:read")
    public ApiResponse<List<TagDTO>> listTags() {
        return new ApiResponse<>(service.list().stream().map(tag -> (TagDTO) tag).toList());
    }

    @GetMapping("/{identifier}")
    @SaCheckPermission("tag:read")
    public ApiResponse<?> queryTag(@PathVariable("identifier") String identifier) {
        Tag tag = service.getTag(identifier);
        if (tag != null) {
            return new ApiResponse<>((TagDTO) tag);
        } else {
            return ApiResponse.notFound();
        }
    }

    @PostMapping("/")
    @SaCheckPermission("tag:write")
    public ApiResponse<Void> writeTag(@RequestBody TagDTO tagDTO) {
        if (!service.saveOrUpdate(tagDTO)) {
            throw new IllegalStateException("Failed to write the tag to database.");
        }
        return ApiResponse.ok();
    }

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
