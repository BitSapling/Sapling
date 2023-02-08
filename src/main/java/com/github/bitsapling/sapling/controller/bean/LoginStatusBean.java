package com.github.bitsapling.sapling.controller.bean;

import com.github.bitsapling.sapling.objects.ResponsePojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
public class LoginStatusBean extends ResponsePojo {
    private final boolean isLoggedIn;
    private final boolean isSafe;
    private final boolean isSwitch;
    private final Map<String, Object> user;

}
