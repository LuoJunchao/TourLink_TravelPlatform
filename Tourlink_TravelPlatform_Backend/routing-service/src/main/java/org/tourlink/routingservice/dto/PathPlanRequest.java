package org.tourlink.routingservice.dto;
import lombok.Data;
import org.tourlink.routingservice.entity.Spot;
import org.tourlink.routingservice.entity.UserPreference;

import java.util.List;

@Data
public class PathPlanRequest {
    private String fromCity;  // 出发城市
    private String toCity;    // 目的城市
    private int maxDays;
    private UserPreference userPreference;
}
