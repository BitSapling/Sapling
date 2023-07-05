package com.github.bitsapling.sapling.module.announcement;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.github.bitsapling.sapling.module.common.CommonService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnouncementService extends ServiceImpl<AnnouncementMapper, Announcement> implements CommonService<Announcement> {

    @NotNull
    public List<Announcement> getLastNAnnouncements(int count) {
        return ChainWrappers.lambdaQueryChain(Announcement.class)
                .orderByDesc(Announcement::getAddedAt)
                .last("LIMIT " + count)
                .list();
    }
}
