package com.github.bitsapling.sapling.service;

import com.github.bitsapling.sapling.entity.PermissionEntity;
import com.github.bitsapling.sapling.entity.UserGroupEntity;
import com.github.bitsapling.sapling.objects.UserGroup;
import com.github.bitsapling.sapling.repository.UserGroupRepository;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserGroupService {
    @Autowired
    private UserGroupRepository repository;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private PromotionService promotionService;

    @Nullable

    public UserGroup getUserGroup(long id) {
        return repository.findById(id).map(userGroupEntity -> new UserGroup(
                userGroupEntity.getId(),
                userGroupEntity.getCode(),
                userGroupEntity.getDisplayName(),
                userGroupEntity.getPermissionEntities().stream()
                        .map(perm -> permissionService.getPermission(perm.getId())).toList(),
                promotionService.convert(userGroupEntity.getPromotionPolicy())
                // , userGroupEntity.getInherited().stream().map(this::convert).toList()
        )).orElse(null);
    }

    public UserGroup save(@NotNull UserGroup userGroup) {
        return convert(repository.save(convert(userGroup)));
    }

    @NotNull
    public UserGroup convert(@NotNull UserGroupEntity group) {
        return new UserGroup(
                group.getId(),
                group.getCode(),
                group.getDisplayName(),
                group.getPermissionEntities().stream()
                        .map(perm -> permissionService.getPermission(perm.getId())).toList(),
                promotionService.convert(group.getPromotionPolicy())
                //, group.getInherited().stream().map(this::convert).toList()
        );
    }

    @NotNull
    public UserGroupEntity convert(@NotNull UserGroup group) {
        return new UserGroupEntity(
                group.getId(),
                group.getCode(),
                group.getDisplayName(),
                group.getPermissionEntities().stream()
                        .map(perm -> new PermissionEntity(
                                perm.getId(),
                                perm.getCode(),
                                perm.isDef()
                        )).toList(),
                promotionService.convert(group.getPromotionPolicy())
                //, group.getInherited().stream().map(this::convert).toList()
        );
    }
}
