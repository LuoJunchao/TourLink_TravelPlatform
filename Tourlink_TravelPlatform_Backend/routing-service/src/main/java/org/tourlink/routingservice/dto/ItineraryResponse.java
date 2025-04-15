package org.tourlink.routingservice.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ItineraryResponse {

    private Long itineraryId;
    private List<NodeResponse> nodes;
    private List<EdgeResponse> edges;
    private Integer totalTimeCost;
    private BigDecimal totalMoneyCost;

}
