//package com.github.bitsapling.sapling.module.failedlogin;
//
//import cn.dev33.satoken.annotation.SaCheckPermission;
//import com.github.bitsapling.sapling.controller.ApiResponse;
//import com.github.bitsapling.sapling.module.category.dto.CategoryDTO;
//import com.github.bitsapling.sapling.module.setting.SettingService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/failed-login")
//@Slf4j
//public class FailedLoginController {
//    @Autowired
//    private FailedLoginService service;
//    @Autowired
//    private SettingService setting;
//
//    @GetMapping("/")
//    @SaCheckPermission("failed-login:read")
//    public ApiResponse<List<CategoryDTO>> listFailedAttempt() {
//        return new ApiResponse<>(service.list().stream().map(c -> (CategoryDTO) c).toList());
//    }
//
//    @GetMapping("/{identifier}")
//    @SaCheckPermission("category:read")
//    public ApiResponse<?> queryCategory(@PathVariable("identifier") String identifier) {
//        FailedLogin category = service.getCategory(identifier);
//        if (category != null) {
//            return new ApiResponse<>((CategoryDTO) category);
//        } else {
//            return ApiResponse.notFound();
//        }
//    }
//
//    @PostMapping("/")
//    @SaCheckPermission("category:write")
//    public ApiResponse<Void> writeCategory(@RequestBody CategoryDTO categoryDTO) {
//        if (!service.saveOrUpdate(categoryDTO)) {
//            throw new IllegalStateException("Failed to write the category to database.");
//        }
//        return ApiResponse.ok();
//    }
//
//
//}
