package org.tourlink.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tourlink.userservice.entity.UserRole;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findByUserId(Long userId);
} 