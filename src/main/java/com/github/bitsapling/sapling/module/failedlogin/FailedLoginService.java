package com.github.bitsapling.sapling.module.failedlogin;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.github.bitsapling.sapling.module.common.CommonService;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@Service
public class FailedLoginService extends ServiceImpl<FailedLoginMapper, FailedLogin> implements CommonService<FailedLogin> {
    public long getFailedAttempts(@NotNull String ip) {
        return ChainWrappers.lambdaQueryChain(FailedLogin.class)
                .eq(FailedLogin::getIp, ip)
                .count();
    }

}
