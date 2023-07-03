package com.github.bitsapling.sapling.autoconfig;

import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApacheUtilsInstanceConfig {
    @Bean
    public InetAddressValidator getIpValidator() {
        return InetAddressValidator.getInstance();
    }

    @Bean
    public URLCodec urlCodec() {
        return new URLCodec();
    }
}
