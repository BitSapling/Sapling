package com.github.bitsapling.sapling.module.advice;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import com.github.bitsapling.sapling.controller.ApiResponse;
import com.github.bitsapling.sapling.module.tracker.exception.FixedAnnounceException;
import com.github.bitsapling.sapling.module.tracker.exception.RetryableAnnounceException;
import com.github.bitsapling.sapling.util.BencodeUtil;
import com.github.bitsapling.sapling.util.ClassUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局控制器拦截
 */
@ControllerAdvice
@Slf4j
public class GlobalControllerAdvice implements ResponseBodyAdvice<ApiResponse<?>> {
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
        return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "处理 API 请求失败",
                Map.of("type", classUtil.getClassSimpleName(exception.getClass()),
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
        return new ApiResponse<>(HttpStatus.BAD_REQUEST, "请求参数无效", exception.getMessage());
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
        return new ApiResponse<>(HttpStatus.UNAUTHORIZED, "该操作要求传递有效登陆状态令牌", exception.getMessage());
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
        return new ApiResponse<>(HttpStatus.FORBIDDEN, "权限不足", exception.getPermission());
    }

    @Override
    public boolean supports(@NotNull MethodParameter returnType, @NotNull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public ApiResponse<?> beforeBodyWrite(ApiResponse<?> body, @NotNull MethodParameter returnType, @NotNull MediaType selectedContentType, @NotNull Class<? extends HttpMessageConverter<?>> selectedConverterType, @NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response) {
        if (body != null) {
            if (body.getHttpCode() != null) {
                response.setStatusCode(body.getHttpCode());
                log.info("响应状态码重写为 " + body.getHttpCode());
            }
        }
        return body;
    }
}
