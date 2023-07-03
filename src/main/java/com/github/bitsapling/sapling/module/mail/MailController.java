package com.github.bitsapling.sapling.module.mail;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.bitsapling.sapling.controller.ApiResponse;
import com.github.bitsapling.sapling.module.mail.dto.MailDTO;
import com.github.bitsapling.sapling.module.mail.dto.MailSendDTO;
import com.github.bitsapling.sapling.module.user.User;
import com.github.bitsapling.sapling.module.user.UserService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/mail")
@Slf4j
@Validated
public class MailController {
    @Autowired
    private MailService service;
    @Autowired
    private UserService userService;

    @GetMapping("/")
    @SaCheckPermission("mail:read")
    public ApiResponse<IPage<?>> listMails(@Validated @Min(1) long current, @Validated @Min(1) @Max(1000) long size) {
        IPage<?> mails = service.getMailsByReceiver(StpUtil.getLoginIdAsLong(), false, Page.of(current, size));
        @SuppressWarnings("rawtypes")
        List records = mails.getRecords().stream().map(mail -> (MailDTO) mail).toList();
        //noinspection unchecked
        mails.setRecords(records);
        return new ApiResponse<>(mails);
    }

    @GetMapping("/{identifier}")
    @SaCheckPermission("mail:read")
    public ApiResponse<?> queryMail(@Validated @PathVariable("identifier") @Min(1) long mailId) {
        Mail mail = service.getById(mailId);
        if (mail == null) {
            return ApiResponse.notFound();
        }
        if (mail.getOwner() != StpUtil.getLoginIdAsLong()) {
            if (!StpUtil.hasPermission("mail:admin-read")) {
                return ApiResponse.forbidden();
            }
        }
        if (mail.getOwner() == StpUtil.getLoginIdAsLong() && mail.getReadedAt() == null) {
            mail.setReadedAt(LocalDateTime.now());
            service.updateById(mail);
        }
        return new ApiResponse<>((MailDTO) mail);
    }

    @DeleteMapping("/{identifier}")
    @SaCheckPermission("mail:read")
    public ApiResponse<?> deleteMail(@Validated @PathVariable("identifier") @Min(1) long mailId) {
        Mail mail = service.getById(mailId);
        if (mail == null) {
            return ApiResponse.notFound();
        }
        if (mail.getOwner() != StpUtil.getLoginIdAsLong()) {
            if (!StpUtil.hasPermission("mail:admin-write")) {
                return ApiResponse.forbidden();
            }
        }
        mail.setDeletedAt(LocalDateTime.now());
        if (!service.updateById(mail)) {
            throw new IllegalStateException("Failed to mark deleted the mail from database.");
        }
        return ApiResponse.ok();
    }

    @GetMapping("/user/{identifier}")
    @SaCheckPermission("mail:admin-read")
    public ApiResponse<?> queryMailByUser(@Validated @PathVariable("identifier") @Min(1) long userId) {
        IPage<?> mails = service.getMailsByReceiver(userId, false, Page.of(1, 1000));
        @SuppressWarnings("rawtypes")
        List records = mails.getRecords().stream().map(mail -> (MailDTO) mail).toList();
        //noinspection unchecked
        mails.setRecords(records);
        return new ApiResponse<>(mails);
    }

    @PostMapping("/")
    @SaCheckPermission("mail:write")
    public ApiResponse<?> sendMail(@RequestBody MailSendDTO mailDto) {
        User sender = userService.getUser(StpUtil.getLoginIdAsLong());
        if (sender == null) {
            throw new IllegalStateException("Failed to get the sender's information even session is valid.");
        }
        long senderId = sender.getId();
        String senderName = sender.getNickname() != null ? sender.getNickname() : sender.getUsername();
        User receiver = userService.getUser(mailDto.getOwner());
        if (receiver == null) {
            return new ApiResponse<>(404, "The receiver is not found.");
        }
        Mail mail = new Mail(0L, receiver.getId(), senderId, senderName, mailDto.getTitle(), mailDto.getDescription(), LocalDateTime.now(),
                null, null);
        if (!service.save(mail)) {
            throw new IllegalStateException("Failed to write the mail to database.");
        }
        return ApiResponse.ok();
    }

    @PostMapping("/mass-send")
    @SaCheckPermission("mail:admin-write")
    public ApiResponse<?> sendMassMail(@RequestBody MailSendDTO mailDto) {
        User sender = userService.getUser(StpUtil.getLoginIdAsLong());
        if (sender == null) {
            throw new IllegalStateException("Failed to get the sender's information even session is valid.");
        }
        long senderId = sender.getId();
        String senderName = sender.getNickname() != null ? sender.getNickname() : sender.getUsername();
        List<Mail> mails = new ArrayList<>();
        for (User receiver : userService.list()) {
            mails.add(new Mail(0L, receiver.getId(), senderId, senderName, mailDto.getTitle(), mailDto.getDescription(), LocalDateTime.now(),
                    null, null));
        }
        if (!service.saveBatch(mails)) {
            throw new IllegalStateException("Failed to write the mails to database.");
        }
        return new ApiResponse<>(200, "Successfully sent " + mails.size() + " mails.");
    }
}