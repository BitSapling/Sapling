package com.github.bitsapling.sapling.shell;

import com.github.bitsapling.sapling.module.setting.SettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;

@ShellComponent
public class InstanceSetupShell {
    private static final Logger LOGGER = LoggerFactory.getLogger(InstanceSetupShell.class);
    @Autowired
    private SettingService setting;
    
}
