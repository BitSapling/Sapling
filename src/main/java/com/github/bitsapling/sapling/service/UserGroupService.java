package com.github.bitsapling.sapling.service;

import com.github.bitsapling.sapling.entity.UserGroup;
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
        return repository.findById(id).map(userGroup -> new UserGroup(
                userGroup.getId(),
                userGroup.getSlug(),
                userGroup.getDisplayName(),
                userGroup.getPermissionEntities().stream()
                        .map(perm -> permissionService.getPermission(perm.getId())).toList(),
                userGroup.getPromotionPolicy()
                // , userGroupEntity.getInherited().stream().map(this::convert).toList()
        )).orElse(null);
    }

    public UserGroup getDefaultUserGroup(){
        return repository.findAll().iterator().next();
    }

    @NotNull
    public UserGroup save(@NotNull UserGroup userGroup) {
        return repository.save(userGroup);
    }

}
