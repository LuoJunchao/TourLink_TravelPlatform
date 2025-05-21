package org.tourlink.routingservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class ItineraryAdjustmentRequest {

    private Long itineraryId; // 要调整的行程ID
    private List<String> newInterests; // 用户新的兴趣列表（可以用来调整景点或活动）
    private Integer newBudget; // 新的预算
    private Integer newTimeBudget; // 新的时间预算
    private List<String> adjustedNodes; // 调整后的节点（如替换景点、酒店等）
    private List<String> adjustedEdges; // 调整后的路径

}
