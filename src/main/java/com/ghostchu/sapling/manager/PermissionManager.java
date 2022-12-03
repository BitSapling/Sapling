package com.ghostchu.sapling.manager;

import org.springframework.stereotype.Component;

@Component
public class PermissionManager {
    public boolean hasPermission(int userId, String node) {
        return true;
    }

    public boolean hasPermission(int userId, Permission node) {
        return hasPermission(userId, node.getNode());
    }
}
