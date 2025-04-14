package org.tourlink.socialservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "blog_like")
public class BlogLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long likeId;

    @ManyToOne
    @JoinColumn(name = "blog_id", referencedColumnName = "blog_id")
    private Blog blog;

    @Column(name = "user_id", nullable = false)
    private String userId;
}
