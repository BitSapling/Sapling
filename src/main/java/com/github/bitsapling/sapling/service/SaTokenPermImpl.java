package com.github.bitsapling.sapling.service;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpInterface;
import com.github.bitsapling.sapling.entity.Permission;
import com.github.bitsapling.sapling.entity.User;
import com.github.bitsapling.sapling.util.HibernateSessionUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@Transactional
public class SaTokenPermImpl implements StpInterface {
    @Autowired
    private UserService userService;
    @Autowired
    private HibernateSessionUtil sessionUtil;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        boolean participate = sessionUtil.bindToThread();
        try {
            User user = userService.getUser(Long.parseLong(String.valueOf(loginId)));
            if (user == null) {
                throw new NotLoginException("You hadn't logged in yet!", loginType, "Not logged in");
            }
            return user.getGroup().getPermissionEntities().stream().map(Permission::getSlug).toList();
        }finally {
            sessionUtil.closeFromThread(participate);
        }
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        boolean participate = sessionUtil.bindToThread();
        try {
            User user = userService.getUser(Long.parseLong(String.valueOf(loginId)));
            if (user == null) {
                throw new NotLoginException("You hadn't logged in yet!", loginType, "Not logged in");
            }
            return List.of(user.getGroup().getSlug());
        }finally {
            sessionUtil.closeFromThread(participate);
        }
    }
}
