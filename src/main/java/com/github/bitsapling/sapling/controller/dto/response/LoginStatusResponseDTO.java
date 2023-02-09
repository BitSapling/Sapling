package com.github.bitsapling.sapling.controller.dto.response;

import com.github.bitsapling.sapling.objects.ResponsePojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
public class LoginStatusResponseDTO extends ResponsePojo {
    private final boolean isLoggedIn;
    private final boolean isSafe;
    private final boolean isSwitch;
    private final UserSessionResponseDTO user;

}
