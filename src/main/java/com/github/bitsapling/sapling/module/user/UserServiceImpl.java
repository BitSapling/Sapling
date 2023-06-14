package com.github.bitsapling.sapling.module.user;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    @Nullable
    public User getUserByUsername(String username) {
        return this.getOne(this.lambdaQuery().eq(User::getUsername, username));
    }

    @Override
    @Nullable
    public User getUserByEmail(String email) {
        return this.getOne(this.lambdaQuery().eq(User::getEmail, email));
    }

    @Override
    @Nullable
    public User getUserByPasskey(String passkey) {
        return this.getOne(this.lambdaQuery().eq(User::getPasskey, passkey));
    }
}
