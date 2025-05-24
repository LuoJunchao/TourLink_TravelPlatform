package org.tourlink.attractionservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tourlink.attractionservice.entity.AttractionReview;
import jakarta.persistence.EntityManager;
import java.util.List;

@RestController
@RequestMapping("/api/attraction-reviews")
public class AttractionReviewController {

    @Autowired
    private EntityManager entityManager;

    @GetMapping
    public ResponseEntity<List<AttractionReview>> getAllReviews() {
        List<AttractionReview> reviews = entityManager.createQuery("SELECT r FROM AttractionReview r", AttractionReview.class)
                .getResultList();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttractionReview> getReviewById(@PathVariable Long id) {
        AttractionReview review = entityManager.find(AttractionReview.class, id);
        if (review == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(review);
    }

    @GetMapping("/attraction/{attractionId}")
    public ResponseEntity<List<AttractionReview>> getReviewsByAttraction(@PathVariable Long attractionId) {
        List<AttractionReview> reviews = entityManager.createQuery(
                "SELECT r FROM AttractionReview r WHERE r.attraction.id = :attractionId", AttractionReview.class)
                .setParameter("attractionId", attractionId)
                .getResultList();
        return ResponseEntity.ok(reviews);
    }

    @PostMapping
    public ResponseEntity<AttractionReview> createReview(@RequestBody AttractionReview review) {
        entityManager.persist(review);
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        AttractionReview review = entityManager.find(AttractionReview.class, id);
        if (review == null) {
            return ResponseEntity.notFound().build();
        }
        entityManager.remove(review);
        return ResponseEntity.ok().build();
    }
} 