package org.tourlink.routingservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "route_step")
public class RouteStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private RoutePlan routePlan;

    @ManyToOne
    private Node node;

    private Integer sequence; // 在行程中的顺序

    private LocalDateTime arriveTime;

    private LocalDateTime leaveTime;

}
