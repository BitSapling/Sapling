package com.github.bitsapling.sapling.module.mail;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.github.bitsapling.sapling.module.common.CommonService;
import org.springframework.stereotype.Service;

@Service
public class MailService extends ServiceImpl<MailMapper, Mail> implements CommonService<Mail> {
    public IPage<Mail> getMailsByReceiver(Long userId, boolean includeDeleted, Page<Mail> mailPage) {
        return ChainWrappers.lambdaQueryChain(Mail.class)
                .eq(Mail::getOwner, userId)
                .and(!includeDeleted, w -> w.isNull(Mail::getDeletedAt))
                .page(mailPage);
    }

    public IPage<Mail> getMailsBySender(Long senderId, boolean includeDeleted, Page<Mail> mailPage) {
        return ChainWrappers.lambdaQueryChain(Mail.class)
                .eq(Mail::getSender, senderId)
                .and(!includeDeleted, w -> w.isNull(Mail::getDeletedAt))
                .page(mailPage);
    }
}
