package com.ghostchu.sapling.service;

import com.ghostchu.sapling.domain.entity.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    /**
     * Gets the User instance from passkey
     *
     * @param passkey user passkey
     * @return The user instance, null for not found
     */
    @Nullable
    public User getUser(@NotNull UUID passkey) {
        return new User();
    }

    /**
     * Update user data into database
     *
     * @param user the user
     */
    public void updateUser(@NotNull User user) {

    }

}
