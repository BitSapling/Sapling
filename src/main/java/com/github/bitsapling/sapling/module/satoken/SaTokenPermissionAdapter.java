package com.github.bitsapling.sapling.module.satoken;

import cn.dev33.satoken.stp.StpInterface;
import com.github.bitsapling.sapling.module.group.Group;
import com.github.bitsapling.sapling.module.group.GroupService;
import com.github.bitsapling.sapling.module.permission.Permission;
import com.github.bitsapling.sapling.module.permission.PermissionService;
import com.github.bitsapling.sapling.module.user.User;
import com.github.bitsapling.sapling.module.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class SaTokenPermissionAdapter implements StpInterface {
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private UserService userService;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        User user = userService.getUser(loginId);
        if (user == null) return Collections.emptyList();
        Group group = groupService.getById(user.getGroup());
        if (group == null) return Collections.emptyList();
        return permissionService.getPermissionByGroup(group.getId()).stream().map(Permission::getPermission).toList();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        User user = userService.getUser(loginId);
        if (user == null) return Collections.emptyList();
        Group group = groupService.getById(user.getGroup());
        return List.of(group.getName());
    }
}