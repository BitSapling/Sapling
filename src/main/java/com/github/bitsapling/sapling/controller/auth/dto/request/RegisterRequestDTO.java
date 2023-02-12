package com.github.bitsapling.sapling.controller.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Validated
public class RegisterRequestDTO {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    @NotEmpty
    @Email
    private String email;
}
