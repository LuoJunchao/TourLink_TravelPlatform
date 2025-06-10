package org.tourlink.userservice.service;

import org.tourlink.userservice.entity.UserProfile;

public interface UserProfileService {
    UserProfile createUserProfile(UserProfile userProfile);
    UserProfile getUserProfileById(Long id);
    UserProfile getUserProfileByUserId(Long userId);
    UserProfile updateUserProfile(Long id, UserProfile userProfile);
    void deleteUserProfile(Long id);
} 