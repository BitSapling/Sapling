package com.github.bitsapling.sapling.module.user;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.bitsapling.sapling.module.common.CommonService;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;

@Service
public class UserMetadataService extends ServiceImpl<UserMetadataMapper, UserMetadata> implements CommonService<UserMetadata> {
    @Nullable
    public UserMetadata getUserMetadataByUserId(Long userId) {
        return getOne(this
                .lambdaQuery()
                .eq(UserMetadata::getUser, userId));
    }

}
