package com.github.bitsapling.sapling.service;

import com.github.bitsapling.sapling.entity.Permission;
import com.github.bitsapling.sapling.repository.PermissionRepository;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Repository
@Transactional
public class PermissionService {
    @Autowired
    private PermissionRepository repository;

    @NotNull
    public Permission registerPermission(@NotNull String code, boolean def) {
        Optional<Permission> permission = repository.findBySlug(code);
        if (permission.isPresent()) {
            Permission entity = permission.get();
            return new Permission(entity.getId(), entity.getSlug(), entity.isDef());
        }
        Permission entity = new Permission(0, code, def);
        entity = repository.save(entity);
        return entity;
    }

    @Nullable
    public Permission getPermission(long id) {
        Optional<Permission> permission = repository.findById(id);
        return permission.orElse(null);
    }

    @Nullable
    public Permission getPermission(@NotNull String code) {
        Optional<Permission> permission = repository.findBySlug(code);
        return permission.orElse(null);
    }

    @NotNull
    public Permission save(@NotNull Permission permission) {
        return repository.save(permission);
    }
}
