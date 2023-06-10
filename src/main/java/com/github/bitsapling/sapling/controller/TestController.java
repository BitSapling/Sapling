package com.github.bitsapling.sapling.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.test.MyTestController;

@RestController
public class TestController {
    @Autowired
    private AbstractAutowireCapableBeanFactory beanFactory;

    @GetMapping("/register")
    public String test() {
        beanFactory.registerSingleton(MyTestController.class.getName(), new MyTestController());
        return "regsitered!";
    }
}
