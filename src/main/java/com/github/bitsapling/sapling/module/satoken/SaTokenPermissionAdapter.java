package com.github.bitsapling.sapling.module.satoken;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.session.SaSessionCustomUtil;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.github.bitsapling.sapling.module.group.Group;
import com.github.bitsapling.sapling.module.group.GroupService;
import com.github.bitsapling.sapling.module.permission.Permission;
import com.github.bitsapling.sapling.module.permission.PermissionService;
import com.github.bitsapling.sapling.module.user.User;
import com.github.bitsapling.sapling.module.user.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

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
        List<String> permissionList = new ArrayList<>();
        for (String roleId : getRoleList(loginId, loginType)) {
            SaSession roleSession = SaSessionCustomUtil.getSessionById("role-" + roleId);
            List<String> list = roleSession.get("Permission_List", () -> readPermissionsFromDatabase(loginId));
            permissionList.addAll(list);
        }
        return permissionList;
    }


    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        SaSession session = StpUtil.getSessionByLoginId(loginId);
        return session.get("Role_List", () -> readRolesFromDatabase(loginId));
    }


    private List<String> readRolesFromDatabase(@NotNull Object loginId) {
        User user = userService.getUser(loginId);
        if (user == null) return Collections.emptyList();
        Group group = groupService.getById(user.getGroup());
        return List.of(group.getName());
    }

    @NotNull
    private List<String> readPermissionsFromDatabase(@NotNull Object loginId) {
        User user = userService.getUser(loginId);
        if (user == null) return Collections.emptyList();
        Group group = groupService.getById(user.getGroup());
        if (group == null) return Collections.emptyList();

        Set<Long> discoveredGroup = new HashSet<>();

        discoveredGroup.add(group.getId());
        Set<Permission> discoveredPermissions = new HashSet<>(permissionService.getPermissionByGroup(group.getId()));
        while (group.getExtend() != null && group.getExtend() > 0 && !discoveredGroup.contains(group.getExtend())) {
            group = groupService.getById(group.getExtend());
            discoveredGroup.add(group.getId());
            discoveredPermissions.addAll(permissionService.getPermissionByGroup(group.getId()));
        }

        return discoveredPermissions.stream().map(Permission::getPermission).toList();
    }
}
