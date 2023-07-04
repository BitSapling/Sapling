package com.github.bitsapling.sapling.module.mail;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.bitsapling.sapling.controller.ApiCode;
import com.github.bitsapling.sapling.controller.ApiResponse;
import com.github.bitsapling.sapling.mbp.PagedResult;
import com.github.bitsapling.sapling.module.mail.dto.DraftedMail;
import com.github.bitsapling.sapling.module.mail.dto.MailModuleDtoMapper;
import com.github.bitsapling.sapling.module.mail.dto.PublishedMailSummary;
import com.github.bitsapling.sapling.module.user.User;
import com.github.bitsapling.sapling.module.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/mail")
@Slf4j
@Valid
@Tag(name = "站内信")
public class MailController {
    private static final MailModuleDtoMapper DTO_MAPPER = MailModuleDtoMapper.INSTANCE;
    @Autowired
    private MailService service;
    @Autowired
    private UserService userService;

    @Operation(summary = "列出自己的所有站内信")
    @GetMapping(value = "/", produces = "application/json")
    @SaCheckPermission("mail:read")
    public ApiResponse<PagedResult<PublishedMailSummary>> listMails(@Min(1) long current, @Min(1) @Max(1000) long size) {
        IPage<Mail> mails = service.getMailsByReceiver(StpUtil.getLoginIdAsLong(), false, Page.of(current, size));
        PagedResult<PublishedMailSummary> result = PagedResult.from(mails, DTO_MAPPER::toPublishedMailSummary);
        return new ApiResponse<>(result);
    }

    @Operation(summary = "查看指定站内信（非管理员查看将自动标记已读）")
    @GetMapping(value = "/{identifier}", produces = "application/json")
    @SaCheckPermission("mail:read")
    public ApiResponse<?> queryMail(@PathVariable("identifier") @Min(1) long mailId) {
        Mail mail = service.getById(mailId);
        if (mail == null) {
            return ApiResponse.notFound();
        }
        if (mail.getOwner() != StpUtil.getLoginIdAsLong()) {
            if (!StpUtil.hasPermission("mail:admin-read")) {
                return ApiResponse.forbidden();
            }
        } else {
            if (mail.getReadedAt() == null) {
                mail.setReadedAt(LocalDateTime.now());
                service.updateById(mail);
            }
        }

        return new ApiResponse<>(DTO_MAPPER.toPublishedMail(mail));
    }

    @Operation(summary = "删除指定站内信")
    @DeleteMapping(value = "/{identifier}", consumes = "application/json", produces = "application/json")
    @SaCheckPermission("mail:read")
    public ApiResponse<?> deleteMail(@PathVariable("identifier") @Min(1) long mailId) {
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
            throw new IllegalStateException("标记站内信删除状态失败");
        }
        return ApiResponse.ok();
    }

    @Operation(summary = "列出指定用户的站内信列表")
    @GetMapping(value = "/user/{identifier}", produces = "application/json")
    @SaCheckPermission("mail:admin-read")
    public ApiResponse<?> queryMailByUser(@PathVariable("identifier") @Min(1) long userId) {
        IPage<Mail> mails = service.getMailsByReceiver(userId, false, Page.of(1, 1000));
        PagedResult<PublishedMailSummary> result = PagedResult.from(mails, DTO_MAPPER::toPublishedMailSummary);
        return new ApiResponse<>(result);
    }

    @Operation(summary = "发送站内信")
    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    @SaCheckPermission("mail:write")
    public ApiResponse<?> sendMail(@RequestBody DraftedMail draftedMail) {
        User sender = userService.getUser(StpUtil.getLoginIdAsLong());
        if (sender == null) {
            throw new IllegalStateException("登录会话有效但无法获取发送者的用户信息");
        }
        long senderId = sender.getId();
        String senderName = sender.getNickname() != null ? sender.getNickname() : sender.getUsername();
        User receiver = userService.getUser(draftedMail.getReceiver());
        if (receiver == null) {
            return new ApiResponse<>(ApiCode.NOT_FOUND.code(), "指定的接收者不存在");
        }
        Mail mail = new Mail(0L, receiver.getId(), senderId, senderName, draftedMail.getTitle(), draftedMail.getDescription(), LocalDateTime.now(),
                null, null);
        if (!service.save(mail)) {
            throw new IllegalStateException("无法将站内信保存到数据库");
        }
        return ApiResponse.ok();
    }

    @Operation(summary = "向所有人批量发送站内信")
    @PostMapping(value = "/mass-send", consumes = "application/json", produces = "application/json")
    @SaCheckPermission("mail:admin-write")
    public ApiResponse<?> sendMassMail(@RequestBody DraftedMail draftedMail) {
        User sender = userService.getUser(StpUtil.getLoginIdAsLong());
        if (sender == null) {
            throw new IllegalStateException("登录会话有效但无法获取发送者的用户信息");
        }
        long senderId = sender.getId();
        String senderName = sender.getNickname() != null ? sender.getNickname() : sender.getUsername();
        List<Mail> mails = new ArrayList<>();
        for (User receiver : userService.list()) {
            mails.add(new Mail(0L, receiver.getId(), senderId, senderName, draftedMail.getTitle(), draftedMail.getDescription(), LocalDateTime.now(),
                    null, null));
        }
        if (!service.saveBatch(mails)) {
            throw new IllegalStateException("无法将站内信保存到数据库");
        }
        return new ApiResponse<>(ApiCode.OK.code(), "成功发送了 " + mails.size() + " 封站内信.");
    }
}