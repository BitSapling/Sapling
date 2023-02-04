package com.github.bitsapling.sapling.controller.advice;

import com.github.bitsapling.sapling.exception.BrowserReadableAnnounceException;
import com.github.bitsapling.sapling.exception.FixedAnnounceException;
import com.github.bitsapling.sapling.exception.RetryableAnnounceException;
import com.github.bitsapling.sapling.util.BencodeUtil;
import com.github.bitsapling.sapling.util.ClassUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String announceExceptionHandler(FixedAnnounceException exception) {
        Map<String, String> dict = new HashMap<>();
        dict.put("failure reason", classUtil.getClassSimpleName(exception.getClass()) + ": " + exception.getMessage());
        dict.put("retry in", "never");
        return new String(BencodeUtil.bittorrent().encode(dict), BencodeUtil.bittorrent().getCharset());
    }

    @ExceptionHandler(value = RetryableAnnounceException.class)
    @ResponseBody
    public String announceExceptionHandler(RetryableAnnounceException exception) {
        Map<String, String> dict = new HashMap<>();
        dict.put("failure reason", classUtil.getClassSimpleName(exception.getClass()) + ": " + exception.getMessage());
        dict.put("retry in", String.valueOf(exception.getRetryIn()));
        return new String(BencodeUtil.bittorrent().encode(dict), BencodeUtil.bittorrent().getCharset());
    }

    @ExceptionHandler(value = BrowserReadableAnnounceException.class)
    @ResponseBody
    public String announceExceptionHandler(BrowserReadableAnnounceException exception) {
        return exception.getMessage();
    }
}
