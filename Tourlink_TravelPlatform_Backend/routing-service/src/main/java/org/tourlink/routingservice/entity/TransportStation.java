package org.tourlink.routingservice.entity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "transport_station")
public class TransportStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type; // "TRAIN" 或 "AIRPORT"

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(name = "city_name", nullable = false)
    private String cityName;
}
