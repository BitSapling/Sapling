package com.github.bitsapling.sapling.service;

import com.github.bitsapling.sapling.entity.User;
import com.github.bitsapling.sapling.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
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
    @Cacheable(cacheNames = {"user_passkey"}, key = "#passkey")
    public User getUserByPasskey(String passkey) {
        Optional<User> userEntity = repository.findByPasskey(passkey);
        return userEntity.orElse(null);
    }

    public User save(User user) {
        return repository.save(user);
    }
    
}
