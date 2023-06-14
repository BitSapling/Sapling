package com.github.bitsapling.sapling.module.announcement;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnnouncementService {
    @Autowired
    private AnnouncementMapper mapper;

    @NotNull
    public List<Announcement> getAnnouncements(int count) {
        LambdaQueryWrapper<Announcement> wrapper = Wrappers
                .lambdaQuery(Announcement.class)
                .le(Announcement::getEndedAt, LocalDateTime.now())
                .orderByDesc(Announcement::getAddedAt)
                .last("LIMIT " + count);
        return mapper.selectList(wrapper);
    }

    public int addAnnouncement(@NotNull Announcement announcement) {
        return mapper.insert(announcement);
    }

    public int deleteAnnouncement(@NotNull Announcement announcement) {
        return mapper.deleteById(announcement.getId());
    }

    public int deleteAnnouncement(@NotNull Long id) {
        return mapper.deleteById(id);
    }
}
