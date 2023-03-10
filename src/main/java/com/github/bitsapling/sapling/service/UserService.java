package com.github.bitsapling.sapling.service;

import com.github.bitsapling.sapling.entity.User;
import com.github.bitsapling.sapling.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class UserService {
    @Autowired
    private UserRepository repository;

    // getUser
    @Nullable
    public User getUser(long id) {
        Optional<User> userEntity = repository.findById(id);
        return userEntity.orElse(null);
    }

    @Nullable
    public User getUserByUsername(String username) {
        Optional<User> userEntity = repository.findByUsername(username);
        return userEntity.orElse(null);
    }

    @Nullable
    public User getUserByEmail(String email) {
        Optional<User> userEntity = repository.findByEmail(email);
        return userEntity.orElse(null);
    }

    @Nullable
    public User getUserByPasskey(String passkey) {
        Optional<User> userEntity = repository.findByPasskeyIgnoreCase(passkey);
        return userEntity.orElse(null);
    }
    @Nullable
    public User getUserByPersonalAccessToken(@NotNull String personalAccessToken){
        Optional<User> userEntity = repository.findByPersonalAccessTokenIgnoreCase(personalAccessToken);
        return userEntity.orElse(null);
    }

    @NotNull
    public User save(User user) {
        return repository.save(user);
    }

}
