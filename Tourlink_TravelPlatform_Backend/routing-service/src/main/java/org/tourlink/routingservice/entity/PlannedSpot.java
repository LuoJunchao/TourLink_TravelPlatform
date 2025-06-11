package org.tourlink.routingservice.entity;

import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
@Table(name = "planned_spot")
public class PlannedSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "route_id", nullable = false)
    private PlannedRoute route;

    @ManyToOne
    @JoinColumn(name = "spot_id", nullable = false)
    private Spot spot;

    @Column(name = "assigned_time_slot")
    private String assignedTimeSlot; // 如 "上午", "下午"
}
