package org.tourlink.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tourlink.userservice.entity.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    UserProfile findByUserId(Long userId);
} 