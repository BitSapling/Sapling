package com.github.bitsapling.sapling.module.announcement.dto;

import com.github.bitsapling.sapling.module.announcement.Announcement;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AnnouncementModuleMapper {
    AnnouncementModuleMapper INSTANCE = Mappers.getMapper(AnnouncementModuleMapper.class);

    PublishedAnnouncement toPublishedObject(Announcement announcement);
}
