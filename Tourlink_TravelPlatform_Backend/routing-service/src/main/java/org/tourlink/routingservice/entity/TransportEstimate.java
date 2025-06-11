package org.tourlink.routingservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "transport_estimate")
public class TransportEstimate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from_station", nullable = false)
    private String fromStation;

    @Column(name = "to_station", nullable = false)
    private String toStation;

    @Column(name = "transport_type", nullable = false)
    private String transportType; // "TRAIN" 或 "FLIGHT"

    @Column(name = "estimated_distance", nullable = false)
    private double estimatedDistance;

    @Column(name = "estimated_price", nullable = false)
    private double estimatedPrice;
}
