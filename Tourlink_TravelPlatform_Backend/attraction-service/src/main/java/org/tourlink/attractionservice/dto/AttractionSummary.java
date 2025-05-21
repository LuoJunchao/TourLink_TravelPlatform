package org.tourlink.attractionservice.dto;

import lombok.Data;
import org.tourlink.attractionservice.entity.Attraction;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AttractionSummary {
    private Long attractionId;
    private String province;
    private String name;
    private String starLevel;
    private BigDecimal rating;
    private BigDecimal price;
    private Integer salesVolume;
    private String region;
    private Boolean isFree;
    private String recommendedPlayTime;
    private Integer viewCount;
    private String mainImage; // 主图片，用于列表展示

    // 兼容旧代码
    private BigDecimal averageRating;
    private Integer reviewCount;

    public static AttractionSummary fromEntity(Attraction attraction) {
        AttractionSummary summary = new AttractionSummary();
        summary.setAttractionId(attraction.getAttractionId());
        summary.setProvince(attraction.getProvince());
        summary.setName(attraction.getName());
        summary.setStarLevel(attraction.getStarLevel());
        summary.setRating(attraction.getRating());
        summary.setPrice(attraction.getPrice());
        summary.setSalesVolume(attraction.getSalesVolume());
        summary.setRegion(attraction.getRegion());
        summary.setIsFree(attraction.getIsFree());
        summary.setRecommendedPlayTime(attraction.getRecommendedPlayTime());
        summary.setViewCount(attraction.getViewCount());

        // 兼容旧代码
        summary.setAverageRating(attraction.getAverageRating());
        summary.setReviewCount(attraction.getReviewCount());

        // 设置主图片（如果有）
        List<String> images = attraction.getImages();
        if (images != null && !images.isEmpty()) {
            summary.setMainImage(images.get(0));
        }

        return summary;
    }
}
