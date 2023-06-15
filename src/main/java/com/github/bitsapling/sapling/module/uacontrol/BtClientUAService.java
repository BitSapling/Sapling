package com.github.bitsapling.sapling.module.uacontrol;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.bitsapling.sapling.module.common.CommonService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BtClientUAService extends ServiceImpl<BtClientUAMapper, BtClientUA> implements CommonService<BtClientUA> {

    @NotNull List<BtClientUA> getAllRules(boolean onlyEnabled) {
        LambdaQueryWrapper<BtClientUA> wrapper = Wrappers
                .lambdaQuery(BtClientUA.class)
                .eq(onlyEnabled, BtClientUA::getEnabled, true);
        return baseMapper.selectList(wrapper);
    }
}
