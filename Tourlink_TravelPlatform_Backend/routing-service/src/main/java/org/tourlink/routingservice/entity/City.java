package org.tourlink.routingservice.entity;


import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

import java.awt.*;

@Data
@Entity
@Table(name = "city")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "city_id")
    private Long cityId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "province")
    private String province;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Node> nodes;

    // Getters and Setters
}
