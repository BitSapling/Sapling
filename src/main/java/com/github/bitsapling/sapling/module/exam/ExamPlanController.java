package com.github.bitsapling.sapling.module.exam;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.github.bitsapling.sapling.controller.ApiResponse;
import com.github.bitsapling.sapling.module.exam.dto.DraftedExamPlan;
import com.github.bitsapling.sapling.module.exam.dto.ExamModuleDtoMapper;
import com.github.bitsapling.sapling.module.exam.dto.PublishedExamPlan;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exam-plan")
@Slf4j
@Tag(name = "考核计划")
public class ExamPlanController {
    private static final ExamModuleDtoMapper DTO_MAPPER = ExamModuleDtoMapper.INSTANCE;
    @Autowired
    private ExamPlanService service;

    @Operation(summary = "获取所有考核计划列表")
    @GetMapping(value = "/", consumes = "application/json", produces = "application/json")
    @SaCheckPermission("exam-plan:read")
    public ApiResponse<List<PublishedExamPlan>> listingPlans() {
        return new ApiResponse<>(service.list().stream().map(DTO_MAPPER::toPublishedExamPlan).toList());
    }

    @Operation(summary = "获取指定考核计划")
    @GetMapping(value = "/{identifier}", consumes = "application/json", produces = "application/json")
    @SaCheckPermission("exam-plan:read")
    public ApiResponse<?> queryPlan(@PathVariable("identifier") String identifier) {
        ExamPlan examPlan = service.getExamPlan(identifier);
        if (examPlan != null) {
            return new ApiResponse<>(DTO_MAPPER.toPublishedExamPlan(examPlan));
        } else {
            return ApiResponse.notFound();
        }
    }

    @Operation(summary = "创建考核计划")
    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    @SaCheckPermission("exam-plan:write")
    public ApiResponse<Void> writePlan(@RequestBody DraftedExamPlan draftedExamPlan) {
        ExamPlan examPlan = new ExamPlan(0L,
                draftedExamPlan.getName(),
                draftedExamPlan.getDuration(),
                draftedExamPlan.getTargetUploaded(),
                draftedExamPlan.getTargetDownloaded(),
                draftedExamPlan.getTargetRealUploaded(),
                draftedExamPlan.getTargetRealDownloaded(),
                draftedExamPlan.getTargetKarma(),
                draftedExamPlan.getTargetShareRatio());
        if (!service.save(examPlan)) {
            throw new IllegalStateException("无法向数据库写入考核计划");
        }
        return ApiResponse.ok();
    }

    @Operation(summary = "更新考核计划")
    @PutMapping(value = "/{identifier}", consumes = "application/json", produces = "application/json")
    @SaCheckPermission("exam-plan:write")
    public ApiResponse<Void> updatePlan(@PathVariable("identifier") String identifier, @RequestBody DraftedExamPlan draftedExamPlan) {
        ExamPlan examPlan = service.getExamPlan(identifier);
        if (examPlan == null) {
            return ApiResponse.notFound();
        }
        examPlan.setName(draftedExamPlan.getName());
        examPlan.setDuration(draftedExamPlan.getDuration());
        examPlan.setTargetUploaded(draftedExamPlan.getTargetUploaded());
        examPlan.setTargetDownloaded(draftedExamPlan.getTargetDownloaded());
        examPlan.setTargetRealUploaded(draftedExamPlan.getTargetRealUploaded());
        examPlan.setTargetRealDownloaded(draftedExamPlan.getTargetRealDownloaded());
        examPlan.setTargetKarma(draftedExamPlan.getTargetKarma());
        examPlan.setTargetShareRatio(draftedExamPlan.getTargetShareRatio());
        if (!service.updateById(examPlan)) {
            throw new IllegalStateException("Failed to update the ExamPlan to database");
        }
        return ApiResponse.ok();
    }

    @DeleteMapping(value = "/{identifier}", consumes = "application/json", produces = "application/json")
    @SaCheckPermission("exam-plan:write")
    public ApiResponse<Void> deletePlan(@PathVariable("identifier") String identifier) {
        ExamPlan examPlan = service.getExamPlan(identifier);
        if (examPlan == null) {
            return ApiResponse.notFound();
        }
        if (!service.removeById(examPlan)) {
            throw new IllegalStateException("Failed to delete the ExamPlan from database");
        }
        return ApiResponse.ok();
    }

}
