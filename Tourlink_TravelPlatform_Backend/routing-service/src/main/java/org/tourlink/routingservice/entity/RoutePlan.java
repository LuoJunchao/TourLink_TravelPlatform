package org.tourlink.routingservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "route_plan")
public class RoutePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private String title;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    @Column(name = "budget_time")
    private Integer budgetTime; // 用户可用时间（分钟）

    @Column(name = "budget_money")
    private BigDecimal budgetMoney;

    private String weatherCondition; // 出发时天气（用于记录/重规划依据）

    private String interestTagsJson; // 兴趣关键词 JSON（也可设计成关联表）

}
