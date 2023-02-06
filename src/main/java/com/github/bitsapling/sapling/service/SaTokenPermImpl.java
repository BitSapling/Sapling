package com.github.bitsapling.sapling.service;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpInterface;
import com.github.bitsapling.sapling.entity.Permission;
import com.github.bitsapling.sapling.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SaTokenPermImpl implements StpInterface {
    @Autowired
    private UserService userService;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        if (!(loginId instanceof Long uid)) {
            throw new IllegalArgumentException("LoginID must be a Long user id");
        }
        User user = userService.getUser(uid);
        if (user == null) {
            throw new NotLoginException("You hadn't logged in yet!", loginType, "Not logged in");
        }
        return user.getGroup().getPermissionEntities().stream().map(Permission::getCode).toList();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        if (!(loginId instanceof Long uid)) {
            throw new IllegalArgumentException("LoginID must be a Long user id");
        }
        User user = userService.getUser(uid);
        if (user == null) {
            throw new NotLoginException("You hadn't logged in yet!", loginType, "Not logged in");
        }
        return List.of(user.getGroup().getCode());
    }
}
