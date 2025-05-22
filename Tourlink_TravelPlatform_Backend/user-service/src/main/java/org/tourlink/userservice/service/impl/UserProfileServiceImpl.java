package org.tourlink.userservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tourlink.userservice.entity.UserProfile;
import org.tourlink.userservice.repository.UserProfileRepository;
import org.tourlink.userservice.service.UserProfileService;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Override
    @Transactional
    public UserProfile createUserProfile(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    @Override
    public UserProfile getUserProfileById(Long id) {
        return userProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserProfile not found with id: " + id));
    }

    @Override
    public UserProfile getUserProfileByUserId(Long userId) {
        return userProfileRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public UserProfile updateUserProfile(Long id, UserProfile userProfile) {
        UserProfile existingProfile = getUserProfileById(id);
        existingProfile.setRealName(userProfile.getRealName());
        existingProfile.setPhoneNumber(userProfile.getPhoneNumber());
        existingProfile.setEmail(userProfile.getEmail());
        existingProfile.setAvatar(userProfile.getAvatar());
        existingProfile.setBio(userProfile.getBio());
        return userProfileRepository.save(existingProfile);
    }

    @Override
    @Transactional
    public void deleteUserProfile(Long id) {
        userProfileRepository.deleteById(id);
    }
} 