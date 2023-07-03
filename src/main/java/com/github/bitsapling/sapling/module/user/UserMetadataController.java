package com.github.bitsapling.sapling.module.user;

import com.github.bitsapling.sapling.controller.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/metadata")
@Slf4j
public class UserMetadataController {
    @Autowired
    private UserMetadataService service;
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ApiResponse<List<UserMetadata>> listUserMetadatas() {
        return new ApiResponse<>(service.list());
    }

    @GetMapping("/{userIdentifier}")
    public ApiResponse<?> queryUserMetadata(@PathVariable("userIdentifier") String userIdentifier) {
        User user = userService.getUser(userIdentifier);
        if (user != null) {
            UserMetadata userMetadata = service.getUserMetadataByUserId(user.getId());
            if (userMetadata != null) {
                return new ApiResponse<>(userMetadata);
            } else {
                return new ApiResponse<>(404, "User metadata not found.");
            }
        } else {
            return new ApiResponse<>(404, "User not found.");
        }
    }

}
