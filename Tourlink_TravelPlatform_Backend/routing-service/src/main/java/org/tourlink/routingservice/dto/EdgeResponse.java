package org.tourlink.routingservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class EdgeResponse {

    private Long edgeId;
    private String fromNodeId;
    private String toNodeId;
    private Integer distance;
    private Integer timeCost;
    private BigDecimal moneyCost;

}
