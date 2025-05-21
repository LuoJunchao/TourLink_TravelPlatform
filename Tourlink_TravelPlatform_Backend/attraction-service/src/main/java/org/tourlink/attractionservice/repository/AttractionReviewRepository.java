package org.tourlink.attractionservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tourlink.attractionservice.entity.AttractionReview;

import java.util.List;

@Repository
public interface AttractionReviewRepository extends JpaRepository<AttractionReview, Long> {
    List<AttractionReview> findByAttractionId(Long attractionId);
    List<AttractionReview> findByUserId(Long userId);
}
