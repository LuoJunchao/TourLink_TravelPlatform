package org.tourlink.attractionservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tourlink.attractionservice.entity.AttractionPopularity;
import org.tourlink.attractionservice.repository.AttractionPopularityRepository;
import org.tourlink.attractionservice.service.AttractionPopularityService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AttractionPopularityServiceImpl implements AttractionPopularityService {

    @Autowired
    private AttractionPopularityRepository popularityRepository;

    @Override
    public List<AttractionPopularity> getAllPopularity() {
        return popularityRepository.findAll();
    }

    @Override
    public AttractionPopularity getPopularityById(Long id) {
        return popularityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Popularity data not found with id: " + id));
    }

    @Override
    public AttractionPopularity getPopularityByAttractionId(Long attractionId) {
        Optional<AttractionPopularity> popularity = popularityRepository.findByAttractionId(attractionId);
        return popularity.orElseThrow(() -> new RuntimeException(
                "Popularity data not found for attraction ID: " + attractionId));
    }

    @Override
    public AttractionPopularity createPopularity(AttractionPopularity popularity) {
        return popularityRepository.save(popularity);
    }

    @Override
    public AttractionPopularity updatePopularityCounts(
            Long id,
            Integer viewCount,
            Integer likeCount,
            Integer shareCount
    ) {
        AttractionPopularity existing = getPopularityById(id);
        if (viewCount != null) existing.setViewCount(viewCount);
        if (likeCount != null) existing.setLikeCount(likeCount);
        if (shareCount != null) existing.setShareCount(shareCount);
        return popularityRepository.save(existing);
    }

    @Override
    public void deletePopularityById(Long id) {
        popularityRepository.deleteById(id);
    }

    @Override
    public AttractionPopularity incrementViewCount(Long attractionId) {
        AttractionPopularity popularity = getPopularityByAttractionId(attractionId);
        popularity.setViewCount(popularity.getViewCount() + 1);
        return popularityRepository.save(popularity);
    }

    @Override
    public AttractionPopularity incrementLikeCount(Long attractionId) {
        AttractionPopularity popularity = getPopularityByAttractionId(attractionId);
        popularity.setLikeCount(popularity.getLikeCount() + 1);
        return popularityRepository.save(popularity);
    }

    @Override
    public AttractionPopularity incrementShareCount(Long attractionId) {
        AttractionPopularity popularity = getPopularityByAttractionId(attractionId);
        popularity.setShareCount(popularity.getShareCount() + 1);
        return popularityRepository.save(popularity);
    }
}