package org.tourlink.routingservice.dto;

import lombok.Data;

@Data
public class CityResponse {

    private Integer cityId;
    private String name;
    private String geoBoundary; // 地理范围（可选）

}
