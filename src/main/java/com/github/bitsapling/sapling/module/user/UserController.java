package com.github.bitsapling.sapling.module.user;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.github.bitsapling.sapling.controller.ApiResponse;
import com.github.bitsapling.sapling.module.user.dto.UserLevelAdminReadOnlyDTO;
import com.github.bitsapling.sapling.module.user.dto.UserLevelNormalReadOnlyDTO;
import com.github.bitsapling.sapling.util.Argon2idPwdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService service;
    @Autowired
    private Argon2idPwdUtil pwdUtil;

    @GetMapping("/")
    @SaCheckPermission("user:list")
    public ApiResponse<List<UserLevelNormalReadOnlyDTO>> listUsers() {
        return new ApiResponse<>(service.list().stream().map(UserLevelNormalReadOnlyDTO::new).toList());
    }

    @GetMapping("/{identifier}")
    @SaCheckPermission("user:read")
    public ApiResponse<?> queryUser(@PathVariable("identifier") String identifier) {
        User user = service.getUser(identifier);
        if (user != null) {
            if (StpUtil.hasPermission("user:admin-read")) {
                return new ApiResponse<>((UserLevelAdminReadOnlyDTO) user);
            } else {
                return new ApiResponse<>(new UserLevelNormalReadOnlyDTO(user));
            }
        } else {
            return ApiResponse.notFound();
        }
    }

    @PutMapping("/{username}")
    @SaCheckPermission("user:admin-update")
    public ApiResponse<?> updateUser(@RequestBody UserLevelAdminReadOnlyDTO userDTO) {
        User user = service.getUserByUsername(userDTO.getUsername());
        if (user == null) {
            return ApiResponse.notFound();
        }
        if (userDTO.getPasskey() != null) {
            user.setPasskey(userDTO.getPasskey());
        }
        if (userDTO.getUsername() != null) {
            user.setUsername(userDTO.getUsername());
        }
        if (userDTO.getNickname() != null) {
            user.setNickname(userDTO.getNickname());
        }
        if (userDTO.getPassword() != null) {
            user.setPassword(pwdUtil.hash(userDTO.getPassword()));
        }
        if (userDTO.getLoginProvider() != null) {
            user.setLoginProvider(userDTO.getLoginProvider());
        }
        if (userDTO.getLoginIdentifier() != null) {
            user.setLoginIdentifier(userDTO.getLoginIdentifier());
        }
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getEmail_confirmed() != null) {
            user.setEmail_confirmed(userDTO.getEmail_confirmed());
        }
        if (userDTO.getGroup() != null) {
            user.setGroup(userDTO.getGroup());
        }
        if (userDTO.getAvatarUrl() != null) {
            user.setAvatarUrl(userDTO.getAvatarUrl());
        }
        if (userDTO.getJoinedAt() != null) {
            user.setJoinedAt(userDTO.getJoinedAt());
        }
        if (userDTO.getLastSeenAt() != null) {
            user.setLastSeenAt(userDTO.getLastSeenAt());
        }
        if (userDTO.getRegisterIp() != null) {
            user.setRegisterIp(userDTO.getRegisterIp());
        }
        if (userDTO.getSiteLang() != null) {
            user.setSiteLang(userDTO.getSiteLang());
        }
        if (userDTO.getBio() != null) {
            user.setBio(userDTO.getBio());
        }
        if (userDTO.getIsBanned() != null) {
            user.setIsBanned(userDTO.getIsBanned());
        }
        if (userDTO.getPreferences() != null) {
            user.setPreferences(userDTO.getPreferences());
        }
        if (!service.saveOrUpdate(userDTO)) {
            throw new IllegalStateException("Failed to update the user in database.");
        }
        return ApiResponse.ok();
    }


}
