package org.tourlink.routingservice.dto;

import lombok.Data;

import java.util.Map;

@Data
public class UserProfileDTO {
    private Map<String, Double> tagWeights;
}