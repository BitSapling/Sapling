package com.github.bitsapling.sapling.service;

import com.github.bitsapling.sapling.entity.UserEntity;
import com.github.bitsapling.sapling.objects.User;
import com.github.bitsapling.sapling.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private UserGroupService userGroupService;

    // getUser
    @Nullable
    @Cacheable(cacheNames = "user", key = "#id")
    public User getUser(long id) {
        Optional<UserEntity> userEntity = repository.findById(id);
        return userEntity.map(this::convert).orElse(null);
    }

    @Nullable
    @Cacheable(cacheNames = "user", key = "'user_name='.concat(#username)")
    public User getUserByUsername(String username) {
        Optional<UserEntity> userEntity = repository.findByUsername(username);
        return userEntity.map(this::convert).orElse(null);
    }

    @Nullable
    @Cacheable(cacheNames = "user", key = "'email='.concat(#email)")
    public User getUserByEmail(String email) {
        Optional<UserEntity> userEntity = repository.findByEmail(email);
        return userEntity.map(this::convert).orElse(null);
    }

    @Nullable
    @Cacheable(cacheNames = "user", key = "'passkey='.concat(#passkey)")
    public User getUserByPasskey(String passkey) {
        Optional<UserEntity> userEntity = repository.findByPasskey(passkey);
        return userEntity.map(this::convert).orElse(null);
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "user", key = "#user.id"),
            @CacheEvict(cacheNames = "user", key = "'user_name='.concat(#user.username)"),
            @CacheEvict(cacheNames = "user", key = "'email='.concat(#user.email)"),
            @CacheEvict(cacheNames = "user", key = "'passkey='.concat(#user.passkey)"),
    })
    public void save(User user) {
        UserEntity entity = convert(user);
        repository.save(entity);
    }

    @NotNull
    public UserEntity convert(User user) {
        return new UserEntity(
                user.getId(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getUsername(),
                userGroupService.convert(user.getGroup()),
                user.getPasskey(),
                Timestamp.from(user.getCreatedAt()),
                user.getAvatar(),
                user.getCustomTitle(),
                user.getSignature(),
                user.getLanguage(),
                user.getDownloadBandwidth(),
                user.getUploadBandwidth(),
                user.getDownloaded(),
                user.getUploaded(),
                user.getRealDownloaded(),
                user.getRealUploaded(),
                user.getIsp(),
                user.getKarma(),
                user.getInviteSlot(),
                user.getSeedingTime());
    }

    @NotNull
    public User convert(@NotNull UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getEmail(),
                entity.getPasswordHash(),
                entity.getUsername(),
                userGroupService.convert(entity.getGroup()),
                entity.getPasskey(),
                entity.getCreatedAt().toInstant(),
                entity.getAvatar(),
                entity.getCustomTitle(),
                entity.getSignature(),
                entity.getLanguage(),
                entity.getDownloadBandwidth(),
                entity.getUploadBandwidth(),
                entity.getDownloaded(),
                entity.getUploaded(),
                entity.getRealDownloaded(),
                entity.getRealUploaded(),
                entity.getIsp(),
                entity.getKarma(),
                entity.getInviteSlot(),
                entity.getSeedingTime());
    }
}
