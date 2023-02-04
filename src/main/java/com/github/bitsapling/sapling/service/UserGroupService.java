package com.github.bitsapling.sapling.service;

import com.github.bitsapling.sapling.entity.PermissionEntity;
import com.github.bitsapling.sapling.entity.UserGroupEntity;
import com.github.bitsapling.sapling.objects.UserGroup;
import com.github.bitsapling.sapling.repository.UserGroupRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
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
                userGroupEntity.getDisplayName(),
                userGroupEntity.getPermissionEntities().stream()
                        .map(perm -> permissionService.getPermission(perm.getId())).toList(),
                promotionService.convert(userGroupEntity.getPromotionPolicy())
        )).orElse(null);
    }

    public void save(@NotNull UserGroup userGroup) {
        repository.save(new UserGroupEntity(
                userGroup.getId(),
                userGroup.getDisplayName(),
                userGroup.getPermissionEntities().stream()
                        .map(perm -> new PermissionEntity(
                                perm.getId(),
                                perm.getCode(),
                                perm.isDef()
                        )).toList(),
                promotionService.convert(userGroup.getPromotionPolicy())
        ));
    }

    @NotNull
    public UserGroup convert(@NotNull UserGroupEntity group) {
        return new UserGroup(
                group.getId(),
                group.getDisplayName(),
                group.getPermissionEntities().stream()
                        .map(perm -> permissionService.getPermission(perm.getId())).toList(),
                promotionService.convert(group.getPromotionPolicy())
        );
    }

    @NotNull
    public UserGroupEntity convert(@NotNull UserGroup group) {
        return new UserGroupEntity(
                group.getId(),
                group.getDisplayName(),
                group.getPermissionEntities().stream()
                        .map(perm -> new PermissionEntity(
                                perm.getId(),
                                perm.getCode(),
                                perm.isDef()
                        )).toList(),
                promotionService.convert(group.getPromotionPolicy())
        );
    }
}
