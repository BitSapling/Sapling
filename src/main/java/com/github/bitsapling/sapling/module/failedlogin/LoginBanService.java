package com.github.bitsapling.sapling.module.failedlogin;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.bitsapling.sapling.module.common.CommonService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LoginBanService extends ServiceImpl<LoginBanMapper, LoginBan> implements CommonService<LoginBan> {
    public boolean isBanned(String ip, LocalDateTime current) {
        return count(lambdaQuery()
                .eq(LoginBan::getIp, ip)
                .and(w -> w.ge(LoginBan::getEndTime, current))) > 0;
    }
}
