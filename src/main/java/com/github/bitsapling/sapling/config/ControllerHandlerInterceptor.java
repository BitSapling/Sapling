package com.github.bitsapling.sapling.config;

import cn.dev33.satoken.stp.StpUtil;
import com.github.bitsapling.sapling.entity.User;
import com.github.bitsapling.sapling.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class ControllerHandlerInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        String token = parseToken(request);
        if(token != null) {
            User user = userService.getUserByPersonalAccessToken(token);
            if (user != null) {
                StpUtil.login(user.getId());
            }
        }
        return true;
    }

    @Override
    public void postHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, ModelAndView modelAndView) throws Exception {
        String token = parseToken(request);
        if(token != null) {
            User user = userService.getUserByPersonalAccessToken(token);
            if (user != null) {
                StpUtil.logout(user.getId());
            }
        }
    }

    @Nullable
    private String parseToken(@Nullable HttpServletRequest request) {
        if (request == null) return null;
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            //request.setAttribute("token", token);
            return header.substring(7);
        }
        return null;
    }
}
