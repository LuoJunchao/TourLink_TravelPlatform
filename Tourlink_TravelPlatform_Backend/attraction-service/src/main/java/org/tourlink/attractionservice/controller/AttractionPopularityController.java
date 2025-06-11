 package org.tourlink.attractionservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tourlink.attractionservice.entity.AttractionPopularity;
import jakarta.persistence.EntityManager;
import org.tourlink.attractionservice.service.AttractionPopularityService;

@RestController
@RequestMapping("/api/attraction-popularity")
public class AttractionPopularityController {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private AttractionPopularityService popularityService;

    @GetMapping("/{attractionId}")
    public ResponseEntity<AttractionPopularity> getPopularity(@PathVariable Long attractionId) {
        return ResponseEntity.ok(popularityService.getPopularityByAttractionId(attractionId));
    }

    @PostMapping("/{attractionId}/view")
    public ResponseEntity<AttractionPopularity> incrementViewCount(@PathVariable Long attractionId) {
        return ResponseEntity.ok(popularityService.incrementViewCount(attractionId));
    }

    @PostMapping("/{attractionId}/like")
    public ResponseEntity<AttractionPopularity> incrementLikeCount(@PathVariable Long attractionId) {
        return ResponseEntity.ok(popularityService.incrementLikeCount(attractionId));
    }

    @PostMapping("/{attractionId}/share")
    public ResponseEntity<AttractionPopularity> incrementShareCount(@PathVariable Long attractionId) {
        return ResponseEntity.ok(popularityService.incrementShareCount(attractionId));
    }
}
