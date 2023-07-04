package com.github.bitsapling.sapling.module.mail.dto;

import com.github.bitsapling.sapling.module.mail.Mail;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MailModuleDtoMapper {
    MailModuleDtoMapper INSTANCE = Mappers.getMapper(MailModuleDtoMapper.class);

    PublishedMailSummary toPublishedMailSummary(Mail mail);

    PublishedMail toPublishedMail(Mail mail);
}
