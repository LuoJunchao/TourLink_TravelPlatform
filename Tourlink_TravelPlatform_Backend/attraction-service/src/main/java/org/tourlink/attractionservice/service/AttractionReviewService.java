package org.tourlink.attractionservice.service;

import org.tourlink.attractionservice.entity.AttractionReview;

import java.util.List;

public interface AttractionReviewService {
    List<AttractionReview> getAllReviews();

    AttractionReview getReviewById(Long id);

    List<AttractionReview> getReviewsByAttractionId(Long attractionId);

    List<AttractionReview> getReviewsByUserId(Long userId);

    AttractionReview createReview(AttractionReview review);

    AttractionReview updateReview(Long id, AttractionReview reviewDetails);

    void deleteReview(Long id);
}