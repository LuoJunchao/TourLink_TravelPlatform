package org.tourlink.userservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tourlink.userservice.entity.User;
import org.tourlink.userservice.entity.UserRole;
import org.tourlink.userservice.repository.UserRoleRepository;
import org.tourlink.userservice.service.UserService; // 假设存在UserService用于查找用户是否存在
import org.tourlink.userservice.service.UserRoleService;

import java.util.List;

@Service
@Transactional
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserService userService; // 确保这个服务已经定义

    @Override
    public UserRole createUserRole(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }

    @Override
    public UserRole getUserRoleById(Long id) {
        return userRoleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserRole not found with id: " + id));
    }

    @Override
    public List<UserRole> getUserRolesByUserId(Long userId) {
        return userRoleRepository.findByUserId(userId);
    }

    @Override
    public UserRole updateUserRole(Long id, UserRole updatedUserRole) {
        UserRole existingUserRole = getUserRoleById(id);

        if (updatedUserRole.getRoleType() != null) {
            existingUserRole.setRoleType(updatedUserRole.getRoleType());
        }

        return userRoleRepository.save(existingUserRole);
    }

    @Override
    public void deleteUserRole(Long id) {
        if (!userRoleRepository.existsById(id)) {
            throw new RuntimeException("UserRole not found with id: " + id);
        }
        userRoleRepository.deleteById(id);
    }

    @Override
    public UserRole assignRoleToUser(Long userId, UserRole.RoleType roleType) {
        User user = userService.getUserById(userId); // 你需要实现UserService中的这个方法
        if (user == null) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        List<UserRole> existingRoles = userRoleRepository.findByUserId(userId);
        for (UserRole role : existingRoles) {
            if (role.getRoleType() == roleType) {
                throw new RuntimeException("User already has the role: " + roleType);
            }
        }

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRoleType(roleType);

        return userRoleRepository.save(userRole);
    }

    @Override
    public void removeRoleFromUser(Long userId, UserRole.RoleType roleType) {
        List<UserRole> roles = userRoleRepository.findByUserId(userId);
        for (UserRole role : roles) {
            if (role.getRoleType() == roleType) {
                userRoleRepository.deleteById(role.getId());
                return;
            }
        }
        throw new RuntimeException("Role " + roleType + " not found for user with id: " + userId);
    }
}