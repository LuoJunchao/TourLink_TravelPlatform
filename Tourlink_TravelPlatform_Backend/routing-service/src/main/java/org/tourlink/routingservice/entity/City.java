package org.tourlink.routingservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.awt.*;

@Data
@Entity
@Table(name = "city")
public class City {
    @Id
    @Column(name = "city_id")
    private Integer cityId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "geo_boundary", columnDefinition = "GEOMETRY")
    private Polygon geoBoundary; // 区域多边形存储城市范围
}
