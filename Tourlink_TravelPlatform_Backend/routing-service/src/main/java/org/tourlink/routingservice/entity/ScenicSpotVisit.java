package org.tourlink.routingservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.Data;

import java.awt.*;
import java.math.BigDecimal;
@Entity
@Table(name = "scenic_spot_visit")
public class ScenicSpotVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "visit_id")
    private Long visitId;

    @ManyToOne
    @JoinColumn(name = "day_id")
    private ItineraryDay day;

    @ManyToOne
    @JoinColumn(name = "node_id")
    private Node scenicSpot;

    @Column(name = "ticket_price")
    private Double ticketPrice;

    @Column(name = "transport_cost")
    private Double transportCost;

    // Getters and Setters
}

