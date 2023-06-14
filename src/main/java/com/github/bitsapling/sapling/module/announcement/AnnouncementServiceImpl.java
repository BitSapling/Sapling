package com.github.bitsapling.sapling.module.announcement;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements AnnouncementService {
   
    @NotNull
    public List<Announcement> getLastNAnnouncements(int count) {
        LambdaQueryWrapper<Announcement> wrapper = Wrappers
                .lambdaQuery(Announcement.class)
                .le(Announcement::getEndedAt, LocalDateTime.now())
                .orderByDesc(Announcement::getAddedAt)
                .last("LIMIT " + count);
        return baseMapper.selectList(wrapper);
    }
}
