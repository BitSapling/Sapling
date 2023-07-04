package com.github.bitsapling.sapling.module.user;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.github.bitsapling.sapling.module.common.CommonService;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<UserMapper, User> implements CommonService<User> {

    @Nullable
    public User getUser(Object identifier) {
        return ChainWrappers.lambdaQueryChain(User.class)
                .eq(User::getId, identifier)
                .or(w -> w.eq(User::getUsername, identifier))
                .or(w -> w.eq(User::getEmail, identifier))
                .or(w -> w.eq(User::getPasskey, identifier)).one();
    }

    @Nullable
    public User getUserByUsername(String username) {
        return ChainWrappers.lambdaQueryChain(User.class).eq(User::getUsername, username).one();
    }

    @Nullable
    public User getUserByEmail(String email) {
        return ChainWrappers.lambdaQueryChain(User.class).eq(User::getEmail, email).one();
    }

    @Nullable
    public User getUserByPasskey(String passkey) {
        return ChainWrappers.lambdaQueryChain(User.class).eq(User::getPasskey, passkey).one();
    }

}
