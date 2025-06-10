package org.tourlink.attractionservice.entity;

import lombok.Data;

import jakarta.persistence.*;

/**
 * 标签实体类
 */
@Data
@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    @Column(nullable = false)
    private String type;
}