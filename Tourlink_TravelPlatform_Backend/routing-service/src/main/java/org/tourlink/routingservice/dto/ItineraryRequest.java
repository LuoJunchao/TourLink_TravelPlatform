package org.tourlink.routingservice.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItineraryRequest {

    private Integer budget; // 预算（单位：金钱）
    private Integer timeBudget; // 时间预算（单位：分钟）
    private List<String> interests; // 用户兴趣的列表（如：景点、购物、餐饮等）
    private Integer startCityId; // 行程的起始城市ID
    private Integer endCityId; // 行程的终止城市ID
    private LocalDateTime startDate; // 行程开始时间
    private LocalDateTime endDate; // 行程结束时间

}
