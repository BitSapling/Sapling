package com.github.bitsapling.sapling.module.uacontrol;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.github.bitsapling.sapling.module.common.CommonService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BtClientUAService extends ServiceImpl<BtClientUAMapper, BtClientUA> implements CommonService<BtClientUA> {

    @SuppressWarnings("SameParameterValue")
    @NotNull List<BtClientUA> getAllRules(boolean onlyEnabled) {
        return ChainWrappers.lambdaQueryChain(BtClientUA.class)
                .eq(onlyEnabled, BtClientUA::getEnabled, true)
                .list();
    }

    public boolean isAllowedClient(String clientUA) {
        if (StringUtils.isEmpty(clientUA)) return false;
        List<BtClientUA> rules = getAllRules(true);
        boolean allowed = false;
        for (BtClientUA rule : rules) {
            boolean rulePassed = switch (rule.getMatchType()) {
                case EQUALS -> clientUA.equals(rule.getUserAgent());
                case CONTAINS -> clientUA.contains(rule.getUserAgent());
                case NOT_CONTAINS -> !clientUA.contains(rule.getUserAgent());
                case START_WITH -> clientUA.startsWith(rule.getUserAgent());
                case END_WITH -> clientUA.endsWith(rule.getUserAgent());
                case REGEX -> clientUA.matches(rule.getUserAgent());
            };
            if (rulePassed) {
                allowed = true;
                break;
            }
        }
        return allowed;
    }
}
