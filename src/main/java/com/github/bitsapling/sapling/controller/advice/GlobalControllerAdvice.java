package com.github.bitsapling.sapling.controller.advice;

import com.dampcake.bencode.Bencode;
import com.github.bitsapling.sapling.exception.BrowserReadableAnnounceException;
import com.github.bitsapling.sapling.exception.FixedAnnounceException;
import com.github.bitsapling.sapling.exception.RetryableAnnounceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalControllerAdvice {
    private static final Bencode BITTORRENT_STANDARD_BENCODE_ENCODER = new Bencode(StandardCharsets.ISO_8859_1);
    @ExceptionHandler(value = FixedAnnounceException.class)
    @ResponseBody
    public String announceExceptionHandler(FixedAnnounceException exception){
        Map<String, String> dict = new HashMap<>();
        dict.put("failure reason", exception.getMessage());
        dict.put("retry in", "never");
        return new String(BITTORRENT_STANDARD_BENCODE_ENCODER.encode(dict), BITTORRENT_STANDARD_BENCODE_ENCODER.getCharset());
    }
    @ExceptionHandler(value = RetryableAnnounceException.class)
    @ResponseBody
    public String announceExceptionHandler(RetryableAnnounceException exception){
        Map<String, String> dict = new HashMap<>();
        dict.put("failure reason", exception.getMessage());
        dict.put("retry in", String.valueOf(exception.getRetryIn()));
        return new String(BITTORRENT_STANDARD_BENCODE_ENCODER.encode(dict), BITTORRENT_STANDARD_BENCODE_ENCODER.getCharset());
    }
    @ExceptionHandler(value = BrowserReadableAnnounceException.class)
    @ResponseBody
    public String announceExceptionHandler(BrowserReadableAnnounceException exception){
        return exception.getMessage();
    }
}
