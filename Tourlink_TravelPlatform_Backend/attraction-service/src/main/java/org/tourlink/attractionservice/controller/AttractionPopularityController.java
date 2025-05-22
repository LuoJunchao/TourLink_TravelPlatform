 package org.tourlink.attractionservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tourlink.attractionservice.entity.AttractionPopularity;
import jakarta.persistence.EntityManager;

@RestController
@RequestMapping("/api/attraction-popularity")
public class AttractionPopularityController {

    @Autowired
    private EntityManager entityManager;

    @GetMapping("/{attractionId}")
    public ResponseEntity<AttractionPopularity> getPopularity(@PathVariable Long attractionId) {
        AttractionPopularity popularity = entityManager.createQuery(
                "SELECT p FROM AttractionPopularity p WHERE p.attraction.id = :attractionId", AttractionPopularity.class)
                .setParameter("attractionId", attractionId)
                .getSingleResult();
        return ResponseEntity.ok(popularity);
    }

    @PostMapping("/{attractionId}/view")
    public ResponseEntity<AttractionPopularity> incrementViewCount(@PathVariable Long attractionId) {
        AttractionPopularity popularity = entityManager.createQuery(
                "SELECT p FROM AttractionPopularity p WHERE p.attraction.id = :attractionId", AttractionPopularity.class)
                .setParameter("attractionId", attractionId)
                .getSingleResult();
        popularity.setViewCount(popularity.getViewCount() + 1);
        entityManager.merge(popularity);
        return ResponseEntity.ok(popularity);
    }

    @PostMapping("/{attractionId}/like")
    public ResponseEntity<AttractionPopularity> incrementLikeCount(@PathVariable Long attractionId) {
        AttractionPopularity popularity = entityManager.createQuery(
                "SELECT p FROM AttractionPopularity p WHERE p.attraction.id = :attractionId", AttractionPopularity.class)
                .setParameter("attractionId", attractionId)
                .getSingleResult();
        popularity.setLikeCount(popularity.getLikeCount() + 1);
        entityManager.merge(popularity);
        return ResponseEntity.ok(popularity);
    }

    @PostMapping("/{attractionId}/share")
    public ResponseEntity<AttractionPopularity> incrementShareCount(@PathVariable Long attractionId) {
        AttractionPopularity popularity = entityManager.createQuery(
                "SELECT p FROM AttractionPopularity p WHERE p.attraction.id = :attractionId", AttractionPopularity.class)
                .setParameter("attractionId", attractionId)
                .getSingleResult();
        popularity.setShareCount(popularity.getShareCount() + 1);
        entityManager.merge(popularity);
        return ResponseEntity.ok(popularity);
    }
}