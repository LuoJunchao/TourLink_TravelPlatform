package org.tourlink.routingservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tourlink.routingservice.dto.PathPlanRequest;
import org.tourlink.routingservice.dto.PathPlanResponse;
import org.tourlink.routingservice.service.PathPlanningService;
@RestController
@RequestMapping("/api/path")
public class PathPlanController {

    @Autowired
    private PathPlanningService pathPlanningService;
    @PostMapping("/plan")
    public ResponseEntity<PathPlanResponse> planRoute(@RequestBody PathPlanRequest request) {
        PathPlanResponse result = pathPlanningService.planRoute(request);
        return ResponseEntity.ok(result);
    }
}
