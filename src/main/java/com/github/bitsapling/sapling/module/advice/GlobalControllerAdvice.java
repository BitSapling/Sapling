package com.github.bitsapling.sapling.module.advice;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import com.github.bitsapling.sapling.controller.ApiCode;
import com.github.bitsapling.sapling.controller.ApiResponse;
import com.github.bitsapling.sapling.module.tracker.exception.FixedAnnounceException;
import com.github.bitsapling.sapling.module.tracker.exception.RetryableAnnounceException;
import com.github.bitsapling.sapling.util.BencodeUtil;
import com.github.bitsapling.sapling.util.ClassUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局控制器拦截
 */
@ControllerAdvice
@Slf4j
public class GlobalControllerAdvice {
    @Autowired
    private ClassUtil classUtil;

    /**
     * 拒绝宣告异常处理
     * 向 BitTorrent 客户端返回一个不再重试的宣告请求，表示客户端不应该在未来对该种子再次进行任何宣告尝试
     *
     * @param exception 拒绝宣告异常
     * @return 响应实体
     */
    @ExceptionHandler(value = FixedAnnounceException.class)
    @ResponseBody
    public ResponseEntity<String> announceExceptionHandler(FixedAnnounceException exception) {
        Map<String, String> dict = new HashMap<>();
        dict.put("failure reason", classUtil.getClassSimpleName(exception.getClass()) + ": " + exception.getMessage());
        dict.put("retry in", "never");
        return ResponseEntity.ok()
                .body(BencodeUtil.convertToString(BencodeUtil.bittorrent().encode(dict)));
    }

    /**
     * 宣告重试异常处理
     * 向 BitTorrent 客户端返回一个可重试的宣告请求，表示客户端可以在指定时间之后对该种子再次进行宣告尝试
     *
     * @param exception 宣告重试异常
     * @return 响应实体
     */
    @ExceptionHandler(value = RetryableAnnounceException.class)
    @ResponseBody
    public ResponseEntity<String> announceExceptionHandler(RetryableAnnounceException exception) {
        Map<String, String> dict = new HashMap<>();
        dict.put("failure reason", classUtil.getClassSimpleName(exception.getClass()) + ": " + exception.getMessage());
        dict.put("retry in", String.valueOf(exception.getRetryIn()));
        return ResponseEntity.ok()
                .body(BencodeUtil.convertToString(BencodeUtil.bittorrent().encode(dict)));
    }

    /**
     * API 异常处理
     * 用于捕捉任何未正常处理的 API 错误
     *
     * @param exception 异常
     * @return 响应实体
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ApiResponse<?> apiExceptionHandler(Exception exception) {
        log.error("Catch an API exception", exception);
        return new ApiResponse<>(ApiCode.INTERNAL_ERROR.code(), "Failed to handle API request",
                Map.of("status", "error",
                        "type", classUtil.getClassSimpleName(exception.getClass()),
                        "message", "<hidden for user, see console>"));
    }

    /**
     * 参数异常处理
     *
     * @param exception 参数异常
     * @return 响应实体
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseBody
    public ApiResponse<String> argumentExceptionHandler(IllegalArgumentException exception) {
        return new ApiResponse<>(ApiCode.BAD_REQUEST.code(), "The argument is invalid", exception.getMessage());
    }

    /**
     * 未登录异常处理
     *
     * @param exception 未登录异常
     * @return 响应实体
     */
    @ExceptionHandler(value = NotLoginException.class)
    @ResponseBody
    public ApiResponse<String> loginExceptionHandler(NotLoginException exception) {
        return new ApiResponse<>(ApiCode.UNAUTHORIZED.code(), "The operation required to be logged in", exception.getMessage());
    }

    /**
     * 未授权异常处理
     *
     * @param exception 未授权异常
     * @return 响应实体
     */
    @ExceptionHandler(value = NotPermissionException.class)
    @ResponseBody
    public ApiResponse<String> permissionExceptionHandler(NotPermissionException exception) {
        return new ApiResponse<>(ApiCode.FORBIDDEN.code(), "Permission Denied", exception.getPermission());
    }
}
