package org.tourlink.routingservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.Data;

import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "itinerary_day")
public class ItineraryDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "day_id")
    private Long dayId;

    @ManyToOne
    @JoinColumn(name = "itinerary_id")
    private Itinerary itinerary;

    @Column(name = "day_number")
    private int dayNumber;

    @OneToMany(mappedBy = "day", cascade = CascadeType.ALL)
    private List<ScenicSpotVisit> visits;

    // Getters and Setters
}

