package com.github.bitsapling.sapling.module.exam;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.github.bitsapling.sapling.controller.ApiResponse;
import com.github.bitsapling.sapling.module.exam.dto.ExamPlanDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exam-plan")
@Slf4j
public class ExamPlanController {
    @Autowired
    private ExamPlanService service;

    @GetMapping("/")
    @SaCheckPermission("exam-plan:read")
    public ApiResponse<List<ExamPlanDTO>> listingPlans() {
        return new ApiResponse<>(service.list().stream().map(plan -> (ExamPlanDTO) plan).toList());
    }

    @GetMapping("/{identifier}")
    @SaCheckPermission("exam-plan:read")
    public ApiResponse<?> queryPlan(@PathVariable("identifier") String identifier) {
        ExamPlan examPlan = service.getExamPlan(identifier);
        if (examPlan != null) {
            return new ApiResponse<>((ExamPlanDTO) examPlan);
        } else {
            return ApiResponse.notFound();
        }
    }

    @PostMapping("/")
    @SaCheckPermission("exam-plan:write")
    public ApiResponse<Void> writePlan(@RequestBody ExamPlanDTO examPlanDTO) {
        if (!service.saveOrUpdate(examPlanDTO)) {
            throw new IllegalStateException("Failed to write the ExamPlan to database");
        }
        return ApiResponse.ok();
    }

    @DeleteMapping("/{identifier}")
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
