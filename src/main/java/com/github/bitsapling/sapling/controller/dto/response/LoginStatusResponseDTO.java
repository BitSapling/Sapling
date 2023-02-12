package com.github.bitsapling.sapling.controller.dto.response;

import com.github.bitsapling.sapling.objects.ResponsePojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
@Validated
public class LoginStatusResponseDTO extends ResponsePojo {
    private boolean isLoggedIn;
    private boolean isSafe;
    private boolean isSwitch;
    private UserSessionResponseDTO user;

}
