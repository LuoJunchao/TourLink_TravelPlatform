package org.tourlink.socialservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "blog_recommendation")
public class BlogRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @ManyToOne
    @JoinColumn(name = "blog_id")
    private Blog blog;

    @Column(name = "recommend_time", nullable = false)
    private LocalDateTime recommendTime;

    @Column(name = "reason")
    private String reason; // 可选：热门/你可能感兴趣等

}
