package com.github.bitsapling.sapling.controller.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterRequestDTO {
    private String username;
    private String password;
    private String email;
}
