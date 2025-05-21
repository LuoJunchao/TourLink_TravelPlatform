package org.tourlink.attractionservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tourlink.attractionservice.entity.AttractionPopularity;

import java.util.Optional;

@Repository
public interface AttractionPopularityRepository extends JpaRepository<AttractionPopularity, Long> {
    Optional<AttractionPopularity> findByAttractionId(Long attractionId);
}
