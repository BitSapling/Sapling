package com.github.bitsapling.sapling.module.exam;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.github.bitsapling.sapling.controller.ApiResponse;
import com.github.bitsapling.sapling.module.exam.dto.DraftedExam;
import com.github.bitsapling.sapling.module.exam.dto.ExamModuleDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/exam")
@Slf4j
@Tag(name = "考核")
public class ExamController {
    private static final ExamModuleDtoMapper DTO_MAPPER = ExamModuleDtoMapper.INSTANCE;
    @Autowired
    private ExamService service;

// TODO: 分页
//
//    @Operation(summary = "获取所有考核列表")
//    @GetMapping(value = "/", consumes = "application/json", produces = "application/json")
//    @SaCheckPermission("exam:read")
//    public ApiResponse<List<DeployedExam>> listingExams() {
//        return new ApiResponse<>(service.list().stream().map(DTO_MAPPER::toDeployedExam).toList());
//    }

    @Operation(summary = "获取指定考核计划下的所有考核列表")
    @GetMapping(value = "/{planIdentifier}", consumes = "application/json", produces = "application/json")
    @SaCheckPermission("exam:read")
    public ApiResponse<?> listExamsByPlan(@PathVariable("planIdentifier") String planIdentifier) {
        List<Exam> exam = service.getExamsByPlan(Long.parseLong(planIdentifier));
        return new ApiResponse<>(exam.stream().map(DTO_MAPPER::toDeployedExam).toList());
    }

    @Operation(summary = "获取当前用户的考核")
    @GetMapping(value = "/user", consumes = "application/json", produces = "application/json")
    @SaCheckLogin
    public ApiResponse<?> getExamByUser() {
        Long uid = StpUtil.getLoginIdAsLong();
        Exam exam = service.getExamByUser(uid);
        if (exam == null) {
            return ApiResponse.noContent();
        }
        return new ApiResponse<>(exam);
    }

    @Operation(summary = "获取指定用户的考核")
    @GetMapping(value = "/user/{identifier}", consumes = "application/json", produces = "application/json")
    @SaCheckLogin
    public ApiResponse<?> getExamByUser(@Positive @PathVariable("identifier") Long uid) {
        Exam exam = service.getExamByUser(uid);
        if (exam == null) {
            return ApiResponse.noContent();
        }
        return new ApiResponse<>(DTO_MAPPER.toDeployedExam(exam));
    }

    @Operation(summary = "对指定用户开始或修改考核")
    @PutMapping(value = "/user/{identifier}", consumes = "application/json", produces = "application/json")
    @SaCheckPermission("exam:write")
    public ApiResponse<Void> modifyExam(@Positive @PathVariable("identifier") Long uid, @RequestBody DraftedExam draftedExam) {
        if (!Objects.equals(draftedExam.getUser(), uid)) {
            throw new IllegalArgumentException("请求体中的用户 ID 和 URI 中的用户 ID 不匹配");
        }
        Exam exam = service.getExamByUser(uid);
        if (exam != null) {
            exam.setUser(draftedExam.getUser());
            exam.setStatus(draftedExam.getStatus());
            exam.setStartedAt(draftedExam.getStartedAt());
            exam.setEndAt(draftedExam.getEndAt());
            exam.setExamPlan(draftedExam.getExamPlan());
        } else {
            exam = new Exam(0L, draftedExam.getUser(), draftedExam.getExamPlan(), draftedExam.getStartedAt(), draftedExam.getEndAt(), draftedExam.getStatus());
        }
        if (!service.saveOrUpdate(exam)) {
            throw new IllegalStateException("保存考核到数据库失败");
        }
        return ApiResponse.ok();
    }

    @Operation(summary = "强制结束对指定用户的考核")
    @DeleteMapping(value = "/user/{identifier}", consumes = "application/json", produces = "application/json")
    @SaCheckPermission("exam:write")
    public ApiResponse<Void> deleteExam(@Positive @PathVariable("identifier") Long uid) {
        int changedLines = service.removeExamForUser(uid);
        if (changedLines == 0) {
            return ApiResponse.noContent();
        }
        return ApiResponse.ok();
    }
}
