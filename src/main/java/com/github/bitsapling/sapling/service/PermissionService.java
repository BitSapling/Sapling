package com.github.bitsapling.sapling.service;

import com.github.bitsapling.sapling.entity.PermissionEntity;
import com.github.bitsapling.sapling.objects.Permission;
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
public class PermissionService {
    @Autowired
    private PermissionRepository repository;

    @NotNull
    @Transactional
    public Permission registerPermission(@NotNull String code, boolean def) {
        Optional<PermissionEntity> permission = repository.findByCode(code);
        if (permission.isPresent()) {
            PermissionEntity entity = permission.get();
            return new Permission(entity.getId(), entity.getCode(), entity.isDef());
        }
        PermissionEntity entity = new PermissionEntity(0, code, def);
        entity = repository.save(entity);
        return convert(entity);
    }

    @Nullable
    @Transactional
    public Permission getPermission(long id) {
        Optional<PermissionEntity> permission = repository.findById(id);
        if (permission.isPresent()) {
            PermissionEntity entity = permission.get();
            return convert(entity);
        }
        return null;
    }

    @NotNull
    public Permission convert(@NotNull PermissionEntity entity){
        return new Permission(entity.getId(), entity.getCode(), entity.isDef());
    }

    @Nullable
    @Transactional
    public Permission getPermission(@NotNull String code) {
        Optional<PermissionEntity> permission = repository.findByCode(code);
        if (permission.isPresent()) {
            PermissionEntity entity = permission.get();
            return convert(entity);
        }
        return null;
    }
    @Transactional
    public void save(@NotNull Permission permission) {
        PermissionEntity entity = new PermissionEntity(
                permission.getId(),
                permission.getCode(),
                permission.isDef()
        );
        repository.save(entity);
    }
}
