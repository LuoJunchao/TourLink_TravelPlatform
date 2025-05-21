package org.tourlink.userservice.service;

import org.tourlink.userservice.entity.UserRole;
import java.util.List;

public interface UserRoleService {
    UserRole createUserRole(UserRole userRole);
    UserRole getUserRoleById(Long id);
    List<UserRole> getUserRolesByUserId(Long userId);
    UserRole updateUserRole(Long id, UserRole userRole);
    void deleteUserRole(Long id);
    UserRole assignRoleToUser(Long userId, UserRole.RoleType roleType);
    void removeRoleFromUser(Long userId, UserRole.RoleType roleType);
} 