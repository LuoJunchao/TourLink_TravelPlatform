package org.tourlink.dataplatformservice.entity;

import lombok.Data;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_behavior_log")
public class UserBehaviorLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    /**
     * 操作目标类型：BLOG / ATTRACTION
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false)
    private TargetType targetType;

    /**
     * 操作目标 ID，如博客 ID 或景点 ID
     */
    @Column(name = "target_id", nullable = false)
    private Long targetId;

    /**
     * 行为类型：VIEW / LIKE / COMMENT / COLLECT
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "behavior_type", nullable = false)
    private BehaviorType behaviorType;

    @CreationTimestamp
    @Column(name = "timestamp", updatable = false)
    private LocalDateTime timestamp;

    public enum TargetType {
        BLOG, ATTRACTION
    }

    public enum BehaviorType {
        VIEW, LIKE, COMMENT, COLLECT
    }
}