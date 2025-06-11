package org.tourlink.routingservice.dto;

import lombok.Data;
import org.tourlink.routingservice.entity.PlannedRoute;
import org.tourlink.routingservice.entity.TransportEstimate;

import java.util.List;

@Data
public class PathPlanResponse {
    private List<PlannedRoute> dailyRoutes;
    private TransportEstimate transportEstimate;
}
