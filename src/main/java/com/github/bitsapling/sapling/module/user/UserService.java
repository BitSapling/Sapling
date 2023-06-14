package com.github.bitsapling.sapling.module.user;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jetbrains.annotations.Nullable;

public interface UserService extends IService<User> {


    @Nullable User getUserByUsername(String username);

    @Nullable User getUserByEmail(String email);

    @Nullable User getUserByPasskey(String passkey);
}
