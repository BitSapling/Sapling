package com.github.bitsapling.sapling.controller;

/**
 * API 业务码
 */
public enum ApiCode {
    /**
     * 请求成功完成，并发送了响应
     */
    OK(0),
    /**
     * 用户尝试登陆，但是提供的凭据无效
     */
    AUTHENTICATION_FAILED(1),
    /**
     * 请求成功，资源存在，但无返回内容
     */
    NO_CONTENT(204),
    /**
     * 无效的请求，请求体被拒绝
     */
    BAD_REQUEST(400),
    /**
     * 请求的操作需要登陆，但用户未登录
     */
    UNAUTHORIZED(401),
    /**
     * 权限不足或拒绝访问
     */
    FORBIDDEN(403),
    /**
     * 请求的资源不存在
     */
    NOT_FOUND(404),
    /**
     * 服务器内部错误，请求处理失败
     */
    INTERNAL_ERROR(500);
    private final int code;

    ApiCode(int code) {
        this.code = code;
    }

    public static ApiCode fromCode(int code) {
        for (ApiCode apiCode : ApiCode.values()) {
            if (apiCode.getCode() == code) {
                return apiCode;
            }
        }
        return null;
    }

    public int code() {
        return code;
    }

    public int getCode() {
        return code;
    }
}
