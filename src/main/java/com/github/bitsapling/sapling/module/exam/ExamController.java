package com.github.bitsapling.sapling.module.exam;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.github.bitsapling.sapling.controller.ApiResponse;
import com.github.bitsapling.sapling.module.exam.dto.ExamDTO;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exam")
@Slf4j
public class ExamController {
    @Autowired
    private ExamService service;

    @GetMapping("/")
    @SaCheckPermission("exam:read")
    public ApiResponse<List<ExamDTO>> listingExams() {
        return new ApiResponse<>(service.list().stream().map(exam -> (ExamDTO) exam).toList());
    }

    @GetMapping("/{planIdentifier}")
    @SaCheckPermission("exam:read")
    public ApiResponse<?> listExamsByPlan(@PathVariable("planIdentifier") String planIdentifier) {
        List<Exam> exam = service.getExamsByPlan(Long.parseLong(planIdentifier));
        return new ApiResponse<>(exam.stream().map(ex -> (ExamDTO) ex).toList());
    }

    @GetMapping("/user")
    @SaCheckLogin
    public ApiResponse<?> getExamByUser() {
        Long uid = StpUtil.getLoginIdAsLong();
        Exam exam = service.getExamByUser(uid);
        if (exam == null) {
            return ApiResponse.noContent();
        }
        return new ApiResponse<>(exam);
    }

    @PostMapping("/")
    @SaCheckPermission("exam:write")
    public ApiResponse<Void> startExam(@RequestBody ExamDTO examDTO) {
        if (!service.saveOrUpdate(examDTO)) {
            throw new IllegalStateException("Failed to write the Exam to database");
        }
        return ApiResponse.ok();
    }

    @DeleteMapping("/user/{identifier}")
    @SaCheckPermission("exam:write")
    public ApiResponse<Void> deleteExam(@Positive @PathVariable("identifier") Long uid) {
        int changedLines = service.removeExamForUser(uid);
        if (changedLines == 0) {
            return ApiResponse.noContent();
        }
        return ApiResponse.ok();
    }
}
