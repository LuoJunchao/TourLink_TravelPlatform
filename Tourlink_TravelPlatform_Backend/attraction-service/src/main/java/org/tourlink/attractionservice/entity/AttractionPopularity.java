package org.tourlink.attractionservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "attraction_popularity")
public class AttractionPopularity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "popularity_id")
    private Long popularityId;

    @Column(name = "attraction_id", nullable = false)
    private Long attractionId;

    @Column(name = "view_count", nullable = false)
    private Integer viewCount = 0;

    @Column(name = "like_count", nullable = false)
    private Integer likeCount = 0;

    @Column(name = "share_count", nullable = false)
    private Integer shareCount = 0;

    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        updatedTime = LocalDateTime.now();
    }

    // 为了兼容之前的代码，添加getId方法
    public Long getId() {
        return popularityId;
    }
}