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
    private String userId;    // 新增：用户ID，用于后端调取标签权重
    private UserPreference userPreference; // 这里可以保留，selectedTags 依然由前端传输，tagWeights由后端获取
}
