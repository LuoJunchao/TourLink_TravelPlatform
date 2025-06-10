package org.tourlink.dataplatformservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tourlink.common.dto.dataPlatformDTO.UserProfileDTO;
import org.tourlink.dataplatformservice.service.UserProfileService;

@RestController
@RequestMapping(value = "/data-platform")
@RequiredArgsConstructor
public class DataPlatformController {

    private final UserProfileService userProfileService;

    @GetMapping("/user-profile")
    public ResponseEntity<UserProfileDTO> getUserProfile(@RequestParam("userId") String userId) {
        return ResponseEntity.ok(userProfileService.getUserProfile(userId));
    }

}