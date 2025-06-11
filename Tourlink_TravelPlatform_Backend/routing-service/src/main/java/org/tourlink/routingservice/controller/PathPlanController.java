package org.tourlink.routingservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tourlink.common.dto.dataPlatformDTO.UserProfileDTO;
import org.tourlink.routingservice.client.DataPlatformClient;
import org.tourlink.routingservice.dto.PathPlanRequest;
import org.tourlink.routingservice.dto.PathPlanResponse;
import org.tourlink.routingservice.entity.TransportEstimate;
import org.tourlink.routingservice.entity.TransportStation;
import org.tourlink.routingservice.entity.UserPreference;
import org.tourlink.routingservice.respository.TransportStationRepository;
import org.tourlink.routingservice.service.PathPlanningService;
import org.tourlink.routingservice.utils.GeoUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/path")
public class PathPlanController {

    @Autowired
    private PathPlanningService pathPlanningService;

    @Autowired
    private DataPlatformClient dataPlatformClient;

    @Autowired
    private TransportStationRepository stationRepository;

    @PostMapping("/plan")
    public ResponseEntity<PathPlanResponse> planRoute(@RequestBody PathPlanRequest request) {
        // 用户标签处理
        UserProfileDTO userProfile = dataPlatformClient.getUserProfile(request.getUserId());
        Map<String, Double> tagWeights = (userProfile != null && userProfile.getTagWeights() != null)
                ? userProfile.getTagWeights()
                : Collections.emptyMap();

        if (request.getUserPreference() == null) {
            request.setUserPreference(new UserPreference());
        }
        request.getUserPreference().setTagWeights(tagWeights);

        // 获取交通模式（校验）
        String transportMode = request.getTransportMode();
        if (!"TRAIN".equalsIgnoreCase(transportMode) && !"AIRPORT".equalsIgnoreCase(transportMode)) {
            transportMode = null; // 非法值设为 null
        }

        // 从数据库获取两个城市的交通站
        List<TransportStation> fromStations = stationRepository.findByCityName(request.getFromCity());
        List<TransportStation> toStations = stationRepository.findByCityName(request.getToCity());

        double minDistance = Double.MAX_VALUE;
        TransportStation bestFrom = null, bestTo = null;
        String bestType = null;

        for (TransportStation fs : fromStations) {
            for (TransportStation ts : toStations) {
                if (transportMode != null && !fs.getType().equalsIgnoreCase(transportMode)) continue;
                if (!fs.getType().equalsIgnoreCase(ts.getType())) continue;

                double distance = GeoUtils.calculateDistance(fs.getLatitude(), fs.getLongitude(),
                        ts.getLatitude(), ts.getLongitude());
                if (distance < minDistance) {
                    minDistance = distance;
                    bestFrom = fs;
                    bestTo = ts;
                    bestType = fs.getType();
                }
            }
        }

        // 如果没指定交通方式，自动选择
        if (transportMode == null && minDistance != Double.MAX_VALUE) {
            transportMode = minDistance > 1000 ? "AIRPORT" : "TRAIN";
        }

        // 估算价格
        double price = GeoUtils.estimatePrice(minDistance, transportMode);

        // 构造 TransportEstimate
        TransportEstimate estimate = new TransportEstimate();
        estimate.setFromStation(bestFrom.getName());
        estimate.setToStation(bestTo.getName());
        estimate.setTransportType(transportMode);
        estimate.setEstimatedDistance(minDistance);
        estimate.setEstimatedPrice(price);

        // 调用路径规划服务
        PathPlanResponse result = pathPlanningService.planRoute(request);
        result.setTransportEstimate(estimate);

        return ResponseEntity.ok(result);
    }
}
