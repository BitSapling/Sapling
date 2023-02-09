package com.github.bitsapling.sapling.controller.dto.response;

import cn.dev33.satoken.stp.SaTokenInfo;
import lombok.Data;

@Data
public class UserSessionResponseDTO {
    private SaTokenInfo token;
    private UserResponseDTO user;

    public UserSessionResponseDTO(SaTokenInfo token, UserResponseDTO user) {
        this.token = token;
        this.user = user;
    }
}
