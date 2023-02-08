package com.github.bitsapling.sapling.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.List;
@Data
@AllArgsConstructor
public class MailConfig {
    private String host;
    private int port;
    private boolean login;
    private String username;
    private String password;
    private String sender;
    private boolean emailSuffixWhitelistMode;
    private List<String> emailSuffix;
    private String smtpEncryption;

    @NotNull
    public static String getConfigKey(){
        return "mail";
    }
}
