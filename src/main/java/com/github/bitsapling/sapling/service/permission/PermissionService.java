package com.github.bitsapling.sapling.service.permission;

import com.github.bitsapling.sapling.entity.PermissionEntity;
import com.github.bitsapling.sapling.entity.UserEntity;
import com.github.bitsapling.sapling.entity.UserGroupEntity;
import com.github.bitsapling.sapling.repository.PermissionRepository;
import com.github.bitsapling.sapling.repository.UserGroupRepository;
import com.github.bitsapling.sapling.repository.UserRepository;
import com.google.common.collect.ImmutableList;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Repository
@Transactional
public class PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private UserGroupRepository userGroupRepository;
    @Autowired
    private UserRepository userRepository;

    public List<PermissionEntity> getPermissions(UserEntity user) {
        return ImmutableList.copyOf(user.getGroup().getPermissionEntities());
    }

    public List<PermissionEntity> getPermissions(UserGroupEntity userGroup) {
        return ImmutableList.copyOf(userGroup.getPermissionEntities());
    }

    public List<PermissionEntity> getPermissionsByUser(long userId) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null) return ImmutableList.of();
        return getPermissions(user);
    }

    public List<PermissionEntity> getPermissionsByGroup(long groupId) {
        UserGroupEntity userGroup = userGroupRepository.findById(groupId).orElse(null);
        if (userGroup == null) return ImmutableList.of();
        return getPermissions(userGroup);
    }

    public void setPermission(UserGroupEntity userGroup, PermissionEntity permissionEntity, boolean status) {
        userGroup.getPermissionEntities().removeIf(entity -> entity.getCode().equals(permissionEntity.getCode()));
        if (status) {
            userGroup.getPermissionEntities().add(permissionEntity);
        }
        userGroupRepository.save(userGroup);
    }
}
