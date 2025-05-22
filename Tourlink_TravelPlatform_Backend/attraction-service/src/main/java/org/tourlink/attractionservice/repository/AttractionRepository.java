package org.tourlink.attractionservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tourlink.attractionservice.entity.Attraction;

import jakarta.transaction.Transactional;
import java.util.List;

@Repository
public interface AttractionRepository extends JpaRepository<Attraction, Long> {

    List<Attraction> findByProvince(String province);

    List<Attraction> findByRegion(String region);

    @Query("SELECT a FROM Attraction a WHERE a.name LIKE %:keyword% OR a.description LIKE %:keyword%")
    Page<Attraction> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Attraction a SET a.viewCount = a.viewCount + 1 WHERE a.attractionId = :attractionId")
    void incrementViewCount(@Param("attractionId") Long attractionId);

    List<Attraction> findByIsFeaturedTrue();

    Page<Attraction> findByIsActiveTrue(Pageable pageable);

    @Query("SELECT a FROM Attraction a ORDER BY a.averageRating DESC")
    Page<Attraction> findTopRated(Pageable pageable);

    @Query("SELECT a FROM Attraction a ORDER BY a.viewCount DESC")
    Page<Attraction> findMostViewed(Pageable pageable);
}
