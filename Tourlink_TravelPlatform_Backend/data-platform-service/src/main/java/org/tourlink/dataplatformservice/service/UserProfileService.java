package org.tourlink.dataplatformservice.service;

import org.springframework.http.ResponseEntity;
import org.tourlink.common.dto.dataPlatformDTO.UserProfileDTO;
import org.tourlink.dataplatformservice.entity.UserBehaviorLog;

public interface UserProfileService {

    void updateUserProfile(UserBehaviorLog behaviorLog);

    UserProfileDTO getUserProfile(String userId);
}
