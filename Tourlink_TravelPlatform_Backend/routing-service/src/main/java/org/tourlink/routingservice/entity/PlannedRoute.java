package org.tourlink.routingservice.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
@Data
@Entity
@Table(name = "planned_route")
public class PlannedRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int day;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL)
    private List<PlannedSpot> spots;
}
