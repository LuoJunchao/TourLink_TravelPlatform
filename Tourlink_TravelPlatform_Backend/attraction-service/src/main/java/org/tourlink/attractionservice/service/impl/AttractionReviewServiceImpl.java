package org.tourlink.attractionservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tourlink.attractionservice.entity.AttractionReview;
import org.tourlink.attractionservice.repository.AttractionReviewRepository;
import org.tourlink.attractionservice.service.AttractionReviewService;

import java.util.List;


@Service
@Transactional
public class AttractionReviewServiceImpl implements AttractionReviewService {

    @Autowired
    private AttractionReviewRepository reviewRepository;

    @Override
    public List<AttractionReview> getAllReviews() {
        return reviewRepository.findAll();
    }

    @Override
    public AttractionReview getReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));
    }

    @Override
    public List<AttractionReview> getReviewsByAttractionId(Long attractionId) {
        return reviewRepository.findByAttractionId(attractionId);
    }

    @Override
    public List<AttractionReview> getReviewsByUserId(Long userId) {
        return reviewRepository.findByUserId(userId);
    }

    @Override
    public AttractionReview createReview(AttractionReview review) {
        return reviewRepository.save(review);
    }

    @Override
    public AttractionReview updateReview(Long id, AttractionReview reviewDetails) {
        AttractionReview existingReview = getReviewById(id);

        if (reviewDetails.getContent() != null) {
            existingReview.setContent(reviewDetails.getContent());
        }
        if (reviewDetails.getRating() != null) {
            existingReview.setRating(reviewDetails.getRating());
        }

        return reviewRepository.save(existingReview);
    }

    @Override
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}