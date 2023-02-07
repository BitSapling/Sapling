package com.github.bitsapling.sapling.controller.user;

import com.github.bitsapling.sapling.objects.ResponsePojo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/account")
public class UserRegisterController {

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/register")
    public ResponsePojo register() {
        return new EmailInUseErrorPojo();
    }

}
