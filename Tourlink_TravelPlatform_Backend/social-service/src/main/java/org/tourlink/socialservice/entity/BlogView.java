package org.tourlink.socialservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "blog_view")
public class BlogView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long viewId;

    @ManyToOne
    @JoinColumn(name = "blog_id")
    private Blog blog;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "view_time", nullable = false)
    private LocalDateTime viewTime;

}
