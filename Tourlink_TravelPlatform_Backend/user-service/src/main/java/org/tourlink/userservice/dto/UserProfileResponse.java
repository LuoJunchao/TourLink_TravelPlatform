package org.tourlink.userservice.dto;

import lombok.Data;
import org.tourlink.userservice.entity.UserProfile;

@Data
public class UserProfileResponse {
    private Long id;
    private Long userId;
    private String realName;
    private String phoneNumber;
    private String email;
    private String avatar;
    private String bio;

    public static UserProfileResponse fromEntity(UserProfile profile) {
        UserProfileResponse response = new UserProfileResponse();
        response.setId(profile.getId());
        response.setUserId(profile.getUser().getId());
        response.setRealName(profile.getRealName());
        response.setPhoneNumber(profile.getPhoneNumber());
        response.setEmail(profile.getEmail());
        response.setAvatar(profile.getAvatar());
        response.setBio(profile.getBio());
        return response;
    }
}
