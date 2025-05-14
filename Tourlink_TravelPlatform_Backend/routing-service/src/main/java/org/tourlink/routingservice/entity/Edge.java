package org.tourlink.routingservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "edge")
public class Edge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "edge_id")
    private Long edgeId;

    @ManyToOne
    @JoinColumn(name = "from_node", referencedColumnName = "node_id")
    private Node fromNode;

    @ManyToOne
    @JoinColumn(name = "to_node", referencedColumnName = "node_id")
    private Node toNode;

    @Column(name = "distance", columnDefinition = "INT")
    private Integer distance; // 米

    @Column(name = "time_cost", columnDefinition = "INT")
    private Integer timeCost; // 分钟

    @Column(name = "money_cost", precision = 10, scale = 2)
    private BigDecimal moneyCost;

    @Enumerated(EnumType.STRING)
    @Column(name = "transport_type", columnDefinition = "ENUM('WALKING','BUS','SUBWAY','TAXI','TRAIN','PLANE')")
    private TransportType transportType;

}

