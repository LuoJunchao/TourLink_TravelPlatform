package org.tourlink.attractionservice.dto;

import lombok.Data;
import org.tourlink.attractionservice.entity.Attraction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AttractionResponse {

    private Long attractionId;
    private String province;
    private String name;
    private String starLevel;
    private BigDecimal rating;
    private BigDecimal price;
    private Integer salesVolume;
    private String region;
    private String coordinates;
    private String description;
    private Boolean isFree;
    private String address;
    private List<String> tags;
    private String recommendedPlayTime;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private Integer viewCount;
    private Boolean isActive;
    private Boolean isFeatured;
    private List<String> images;

    // 兼容旧代码
    private BigDecimal averageRating;
    private Integer reviewCount;

    public static AttractionResponse fromEntity(Attraction attraction) {
        AttractionResponse response = new AttractionResponse();
        response.setAttractionId(attraction.getAttractionId());
        response.setProvince(attraction.getProvince());
        response.setName(attraction.getName());
        response.setStarLevel(attraction.getStarLevel());
        response.setRating(attraction.getRating());
        response.setPrice(attraction.getPrice());
        response.setSalesVolume(attraction.getSalesVolume());
        response.setRegion(attraction.getRegion());
        response.setCoordinates(attraction.getCoordinates());
        response.setDescription(attraction.getDescription());
        response.setIsFree(attraction.getIsFree());
        response.setAddress(attraction.getAddress());
        response.setTags(attraction.getTags());
        response.setRecommendedPlayTime(attraction.getRecommendedPlayTime());
        response.setCreatedTime(attraction.getCreatedTime());
        response.setUpdatedTime(attraction.getUpdatedTime());
        response.setViewCount(attraction.getViewCount());
        response.setIsActive(attraction.getIsActive());
        response.setIsFeatured(attraction.getIsFeatured());
        response.setImages(attraction.getImages());

        // 兼容旧代码
        response.setAverageRating(attraction.getAverageRating());
        response.setReviewCount(attraction.getReviewCount());

        return response;
    }
}
