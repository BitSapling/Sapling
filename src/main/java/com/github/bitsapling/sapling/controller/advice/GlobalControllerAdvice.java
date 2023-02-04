package com.github.bitsapling.sapling.controller.advice;

import cn.dev33.satoken.exception.NotLoginException;
import com.github.bitsapling.sapling.exception.FixedAnnounceException;
import com.github.bitsapling.sapling.exception.LoginException;
import com.github.bitsapling.sapling.exception.RetryableAnnounceException;
import com.github.bitsapling.sapling.util.BencodeUtil;
import com.github.bitsapling.sapling.util.ClassUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalControllerAdvice {
    @Autowired
    private ClassUtil classUtil;

    @ExceptionHandler(value = FixedAnnounceException.class)
    @ResponseBody
    public ResponseEntity<String> announceExceptionHandler(FixedAnnounceException exception) {
        Map<String, String> dict = new HashMap<>();
        dict.put("failure reason", classUtil.getClassSimpleName(exception.getClass()) + ": " + exception.getMessage());
        dict.put("retry in", "never");
        return ResponseEntity.ok()
                .body(BencodeUtil.convertToString(BencodeUtil.bittorrent().encode(dict)));
    }

    @ExceptionHandler(value = RetryableAnnounceException.class)
    @ResponseBody
    public ResponseEntity<String> announceExceptionHandler(RetryableAnnounceException exception) {
        Map<String, String> dict = new HashMap<>();
        dict.put("failure reason", classUtil.getClassSimpleName(exception.getClass()) + ": " + exception.getMessage());
        dict.put("retry in", String.valueOf(exception.getRetryIn()));
        return ResponseEntity.ok()
                .body(BencodeUtil.convertToString(BencodeUtil.bittorrent().encode(dict)));
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> apiExceptionHandler(Exception exception) {
        return ResponseEntity.internalServerError()
                .body(
                        Map.of("status", "error",
                                "type", classUtil.getClassSimpleName(exception.getClass()),
                                "message", exception.getMessage())
                );
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> argumentExceptionHandler(IllegalArgumentException exception) {
        return ResponseEntity.internalServerError()
                .body(
                        Map.of("status", "error",
                                "type", classUtil.getClassSimpleName(exception.getClass()),
                                "message", exception.getMessage())
                );
    }

    @ExceptionHandler(value = NotLoginException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> loginExceptionHandler(NotLoginException exception) {
        return ResponseEntity.status(401)
                .body(Map.of("status", "error",
                        "type", classUtil.getClassSimpleName(exception.getClass()),
                        "message", exception.getMessage())
                );
    }

    @ExceptionHandler(value = LoginException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> loginExceptionHandler(LoginException exception) {
        return ResponseEntity.status(401)
                .body(Map.of("status", "error",
                        "type", classUtil.getClassSimpleName(exception.getClass()),
                        "message", exception.getMessage())
                );
    }
}
