package com.github.bitsapling.sapling.objects.setting;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class MailSettings implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
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
