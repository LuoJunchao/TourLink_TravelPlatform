package org.tourlink.attractionservice.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "attraction")
public class Attraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attraction_id")
    private Long attractionId;

    // 为了兼容之前的代码，添加getId方法
    public Long getId() {
        return attractionId;
    }

    // 为了兼容之前的代码，添加setId方法
    public void setId(Long id) {
        this.attractionId = id;
    }

    // 省份
    @Column(name = "province", nullable = false)
    private String province;

    // 名称
    @Column(name = "name", nullable = false)
    private String name;

    // 星级
    @Column(name = "star_level")
    private String starLevel;

    // 评分
    @Column(name = "rating", precision = 3, scale = 1)
    private BigDecimal rating;

    // 平均评分（用于兼容之前的代码）
    @Column(name = "average_rating", precision = 3, scale = 1)
    private BigDecimal averageRating;

    // 价格
    @Column(name = "price", precision = 10, scale = 1)
    private BigDecimal price;

    // 销量
    @Column(name = "sales_volume")
    private Integer salesVolume = 0;

    // 省/市/区
    @Column(name = "region")
    private String region;

    // 坐标
    @Column(name = "coordinates")
    private String coordinates;

    // 简介
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // 是否免费
    @Column(name = "is_free")
    private Boolean isFree = false;

    // 具体地址
    @Column(name = "address")
    private String address;

    // 标签 (使用JSON存储)
    @Convert(converter = StringListConverter.class)
    @Column(name = "tags", columnDefinition = "JSON")
    private List<String> tags = new ArrayList<>();

    // 推荐游玩时间（字符串格式，如"3-4小时"）
    @Column(name = "recommended_play_time")
    private String recommendedPlayTime;

    // 创建时间
    @Column(name = "created_time")
    private LocalDateTime createdTime;

    // 更新时间
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    // 浏览次数
    @Column(name = "view_count")
    private Integer viewCount = 0;

    // 评论数（用于兼容之前的代码）
    @Column(name = "review_count")
    private Integer reviewCount = 0;

    // 是否激活
    @Column(name = "is_active")
    private Boolean isActive = true;

    // 是否推荐
    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    // 图片 URL 列表使用 @Convert 存储为 JSON 字符串
    @Convert(converter = StringListConverter.class)
    @Column(name = "images", columnDefinition = "JSON")
    private List<String> images = new ArrayList<>();

    @OneToMany(mappedBy = "attraction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttractionReview> reviews = new ArrayList<>();

    // 添加评论
    public void addReview(AttractionReview review) {
        reviews.add(review);
        review.setAttraction(this);

        // 更新评分和评论数
        this.reviewCount++;
        this.recalculateAverageRating();
    }

    // 删除评论
    public void removeReview(AttractionReview review) {
        reviews.remove(review);
        review.setAttraction(null);

        // 更新评分和评论数
        this.reviewCount = Math.max(0, this.reviewCount - 1);
        this.recalculateAverageRating();
    }

    // 重新计算平均评分
    private void recalculateAverageRating() {
        if (reviews.isEmpty()) {
            this.averageRating = BigDecimal.ZERO;
            return;
        }

        double sum = reviews.stream()
                .mapToDouble(review -> review.getRating().doubleValue())
                .sum();

        this.averageRating = BigDecimal.valueOf(sum / reviews.size())
                .setScale(1, java.math.RoundingMode.HALF_UP);
    }

    @PrePersist
    protected void onCreate() {
        if (createdTime == null) {
            createdTime = LocalDateTime.now();
        }
        if (updatedTime == null) {
            updatedTime = LocalDateTime.now();
        }
        if (averageRating == null) {
            averageRating = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedTime = LocalDateTime.now();
    }
}

// JSON 列表转换器
@Converter
class StringListConverter implements AttributeConverter<List<String>, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> list) {
        try {
            return objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            return "[]";
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String json) {
        if (json == null || json.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            return new ArrayList<>();
        }
    }
}