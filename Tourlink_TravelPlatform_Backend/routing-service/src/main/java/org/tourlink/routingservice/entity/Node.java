package org.tourlink.routingservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.Data;

import java.awt.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "node")
public class Node {
    @Id
    @Column(name = "node_id")
    private String nodeId;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    @Enumerated(EnumType.STRING)
    @Column(name = "node_type", columnDefinition = "ENUM('SCENIC','HOTEL','TRAIN_STATION','AIRPORT')")
    private NodeType nodeType;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "coordinates", columnDefinition = "POINT")
    private Point coordinates;

    @Column(name = "time_cost", columnDefinition = "INT")
    private Integer timeCost; // 分钟

    @Column(name = "money_cost", precision = 10, scale = 2)
    private BigDecimal moneyCost;
}

