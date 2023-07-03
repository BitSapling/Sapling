package com.github.bitsapling.sapling.module.user;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.bitsapling.sapling.module.common.CommonService;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<UserMapper, User> implements CommonService<User> {

    @Nullable
    public User getUser(Object identifier) {
        return getOne(lambdaQuery()
                .eq(User::getId, identifier)
                .or(w -> w.eq(User::getUsername, identifier))
                .or(w -> w.eq(User::getEmail, identifier))
                .or(w -> w.eq(User::getPasskey, identifier)));
    }

    @Nullable
    public User getUserByUsername(String username) {
        return getOne(lambdaQuery().eq(User::getUsername, username));
    }

    @Nullable
    public User getUserByEmail(String email) {
        return getOne(lambdaQuery().eq(User::getEmail, email));
    }

    @Nullable
    public User getUserByPasskey(String passkey) {
        return getOne(lambdaQuery().eq(User::getPasskey, passkey));
    }

}
