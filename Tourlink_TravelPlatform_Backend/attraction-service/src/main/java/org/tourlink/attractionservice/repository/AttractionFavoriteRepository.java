package org.tourlink.attractionservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tourlink.attractionservice.entity.AttractionFavorite;

import java.util.List;

@Repository
public interface AttractionFavoriteRepository extends JpaRepository<AttractionFavorite, Long> {
    List<AttractionFavorite> findByUserId(Long userId);
}
