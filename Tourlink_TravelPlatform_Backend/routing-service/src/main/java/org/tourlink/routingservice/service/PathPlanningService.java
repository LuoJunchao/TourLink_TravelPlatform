package org.tourlink.routingservice.service;

import org.tourlink.routingservice.dto.PathPlanRequest;
import org.tourlink.routingservice.dto.PathPlanResponse;

public interface PathPlanningService {
    PathPlanResponse planRoute(PathPlanRequest request);
}
