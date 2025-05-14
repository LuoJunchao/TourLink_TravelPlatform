package org.tourlink.routingservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class NodeResponse {

    private String nodeId;
    private String cityId;
    private String nodeType;
    private String name;
    private String coordinates;
    private Integer timeCost;
    private BigDecimal moneyCost;

}
