package com.github.bitsapling.sapling.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class ControllerHandlerConfig implements WebMvcConfigurer {
    @Autowired
    private ControllerHandlerInterceptor controllerHandlerInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(controllerHandlerInterceptor).addPathPatterns("/**");
    }
}
