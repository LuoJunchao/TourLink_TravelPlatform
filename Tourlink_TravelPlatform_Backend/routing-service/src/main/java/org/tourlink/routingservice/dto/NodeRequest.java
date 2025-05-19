package org.tourlink.routingservice.dto;

import java.math.BigDecimal;

public class NodeRequest {

    private Integer cityId; // 城市ID
    private String nodeType; // 节点类型（景点、酒店、车站、机场等）
    private String name; // 节点名称
    private String coordinates; // 坐标（经纬度或其他方式）
    private Integer timeCost; // 该节点的时间成本（单位：分钟）
    private BigDecimal moneyCost; // 该节点的金钱成本

}
