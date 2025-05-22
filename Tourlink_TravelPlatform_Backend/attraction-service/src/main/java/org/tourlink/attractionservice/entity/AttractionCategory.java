package org.tourlink.attractionservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "attraction_category")
public class AttractionCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "icon_url")
    private String iconUrl;

    // 不再与Attraction实体关联
    @Transient // 使用@Transient注解标记此字段不会被持久化到数据库
    private List<Attraction> attractions = new ArrayList<>();
}
