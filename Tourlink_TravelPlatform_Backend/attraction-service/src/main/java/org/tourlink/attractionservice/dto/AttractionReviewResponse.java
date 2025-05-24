package org.tourlink.attractionservice.dto;

import lombok.Data;
import org.tourlink.attractionservice.entity.AttractionReview;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AttractionReviewResponse {
    private Long reviewId;
    private Long attractionId;
    private Long userId;
    private String username; // 可以通过用户服务获取
    private String title;
    private String content;
    private BigDecimal rating;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private Integer likeCount;

    public static AttractionReviewResponse fromEntity(AttractionReview review) {
        AttractionReviewResponse response = new AttractionReviewResponse();
        response.setReviewId(review.getReviewId());
        response.setAttractionId(review.getAttraction().getAttractionId());
        response.setUserId(review.getUserId());
        response.setTitle(review.getTitle());
        response.setContent(review.getContent());
        response.setRating(review.getRating());
        response.setCreatedTime(review.getCreatedTime());
        response.setUpdatedTime(review.getUpdatedTime());
        response.setLikeCount(review.getLikeCount());
        return response;
    }
}
