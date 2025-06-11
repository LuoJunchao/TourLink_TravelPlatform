package org.tourlink.routingservice.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
@Data
@Entity
@Table(name = "spot")
public class Spot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private double rating;

    @Column(nullable = false)
    private int sales;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @ElementCollection
    @CollectionTable(name = "spot_time_slot", joinColumns = @JoinColumn(name = "spot_id"))
    @Column(name = "time_slot")
    private List<String> timeSlots;

    @ElementCollection
    @CollectionTable(name = "spot_tag", joinColumns = @JoinColumn(name = "spot_id"))
    @Column(name = "tag")
    private List<String> tags;
}
