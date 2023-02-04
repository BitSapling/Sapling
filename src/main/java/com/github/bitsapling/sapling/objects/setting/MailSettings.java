package com.github.bitsapling.sapling.objects.setting;

import java.util.List;

public class MailSettings {
    private String host;
    private int port;
    private boolean login;
    private String username;
    private String password;
    private String sender;
    private boolean emailSuffixWhitelistMode;
    private List<String> emailSuffix;
    private String smtpEncryption;

}
