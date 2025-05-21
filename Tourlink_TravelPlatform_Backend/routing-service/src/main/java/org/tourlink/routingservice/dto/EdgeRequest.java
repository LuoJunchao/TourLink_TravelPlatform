package org.tourlink.routingservice.dto;

import java.math.BigDecimal;

public class EdgeRequest {

    private String fromNodeId; // 起始节点ID
    private String toNodeId; // 终止节点ID
    private Integer distance; // 路径距离（单位：米）
    private Integer timeCost; // 路径的时间成本（单位：分钟）
    private BigDecimal moneyCost; // 路径的金钱成本

}
