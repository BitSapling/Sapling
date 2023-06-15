package com.github.bitsapling.sapling.module.uacontrol;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BtClientUAServiceImpl extends ServiceImpl<BtClientUAMapper, BtClientUA> implements BtClientUAService {

    @NotNull List<BtClientUA> getAllRules(boolean onlyEnabled) {
        LambdaQueryWrapper<BtClientUA> wrapper = Wrappers
                .lambdaQuery(BtClientUA.class)
                .eq(onlyEnabled, BtClientUA::getEnabled, true);
        return baseMapper.selectList(wrapper);
    }
}
