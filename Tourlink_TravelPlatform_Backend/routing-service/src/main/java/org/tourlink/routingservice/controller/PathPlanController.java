package org.tourlink.routingservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tourlink.common.dto.dataPlatformDTO.UserProfileDTO;
import org.tourlink.routingservice.client.DataPlatformClient;
import org.tourlink.routingservice.dto.PathPlanRequest;
import org.tourlink.routingservice.dto.PathPlanResponse;
import org.tourlink.routingservice.entity.UserPreference;
import org.tourlink.routingservice.service.PathPlanningService;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/path")
public class PathPlanController {

    @Autowired
    private PathPlanningService pathPlanningService;

    @Autowired
    private DataPlatformClient dataPlatformClient;

    @GetMapping("/status")
    public String checkApiStatus() {
        return "API is working! Status: OK";
    }
    @PostMapping("/plan")
    public ResponseEntity<PathPlanResponse> planRoute(@RequestBody PathPlanRequest request) {

        // 1. 调用数据中台接口获取用户标签权重
        UserProfileDTO userProfile = dataPlatformClient.getUserProfile(request.getUserId());
        Map<String, Double> tagWeights = (userProfile != null && userProfile.getTagWeights() != null)
                ? userProfile.getTagWeights()
                : Collections.emptyMap();

        // 2. 设置到 UserPreference 中
        if (request.getUserPreference() == null) {
            request.setUserPreference(new UserPreference());
        }
        request.getUserPreference().setTagWeights(tagWeights);

        // 3. 调用路径规划服务
        PathPlanResponse result = pathPlanningService.planRoute(request);

        return ResponseEntity.ok(result);
    }
}
