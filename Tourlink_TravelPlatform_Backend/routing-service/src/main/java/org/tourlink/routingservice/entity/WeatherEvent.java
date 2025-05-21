package org.tourlink.routingservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "weather_event")
public class WeatherEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private City city;

    private LocalDateTime eventTime;

    private String description;

    private String type; // 如暴雨、台风、限行等

    private Boolean resolved = false;

}
