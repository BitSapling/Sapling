package com.github.bitsapling.sapling.controller;

import com.github.bitsapling.sapling.objects.Permission;
import com.github.bitsapling.sapling.objects.PromotionPolicy;
import com.github.bitsapling.sapling.objects.User;
import com.github.bitsapling.sapling.objects.UserGroup;
import com.github.bitsapling.sapling.service.PermissionService;
import com.github.bitsapling.sapling.service.PromotionService;
import com.github.bitsapling.sapling.service.UserGroupService;
import com.github.bitsapling.sapling.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController("/debug")
@Slf4j
public class DebugController {
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private PromotionService promotionService;
    @Autowired
    private UserGroupService userGroupService;
    @Autowired
    private UserService userService;

    @GetMapping("/initTables")
    public String initTables() {
        Permission permission = new Permission(0, "torrent:announce", true);
        permissionService.save(permission);
        PromotionPolicy promotionPolicy = new PromotionPolicy(0, "系统默认", 1.0d, 1.0d);
        promotionService.save(promotionPolicy);
        List<Permission> permissions = new ArrayList<>();
        permissions.add(permission);
        UserGroup userGroup = new UserGroup(0, "default", "Lv.1 青铜", permissions, promotionPolicy);
        userGroupService.save(userGroup);
        User user = new User(0,
                "test@test.com",
                "$2a$06$r6QixzXG/Y8mUtmCV7b70.Jp7qjOL2nONUJolzGmQPzVn2acoKLf6",
                "TestUser1",
                userGroup,
                new UUID(0, 0).toString(),
                Instant.now(),
                "https://www.baidu.com/favicon.ico",
                "这是自定义头衔",
                "这是测试签名",
                "zh-CN",
                "1000mbps",
                "1000mbps",
                0,
                0,
                0,
                0,
                "中国移不动",
                BigDecimal.ZERO,
                0,
                Duration.ZERO);
        userService.save(user);
        log.info("创建测试用户 1 成功");
        User user2 = new User(0,
                "test2@test.com",
                "$2a$06$r6QixzXG/Y8mUtmCV7b70.Jp7qjOL2nONUJolzGmQPzVn2acoKLf6",
                "TestUser2",
                userGroup,
                new UUID(0, 0).toString(),
                Instant.now(),
                "https://weibo.com/favicon.ico",
                "这是自定义头衔2",
                "这是测试签名2",
                "en-US",
                "10000mbps",
                "10000mbps",
                0,
                0,
                0,
                0,
                "中国联不通",
                BigDecimal.ZERO,
                5,
                Duration.ZERO);
        userService.save(user2);
        log.info("创建测试用户 2 成功");
        return "初始化基本数据库测试内容成功";
    }
}
