package org.tourlink.attractionservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tourlink.attractionservice.dto.AttractionRequest;
import org.tourlink.attractionservice.dto.AttractionResponse;
import org.tourlink.attractionservice.dto.AttractionSummary;
import org.tourlink.attractionservice.entity.Attraction;
import org.tourlink.attractionservice.repository.AttractionRepository;
import org.tourlink.attractionservice.service.AttractionService;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AttractionServiceImpl implements AttractionService {

    @Autowired
    private AttractionRepository attractionRepository;

    @Override
    public List<AttractionSummary> getAllAttractions() {
        return attractionRepository.findAll().stream()
                .map(AttractionSummary::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Page<AttractionSummary> getAttractionsPaged(Pageable pageable) {
        return attractionRepository.findByIsActiveTrue(pageable)
                .map(AttractionSummary::fromEntity);
    }

    @Override
    public AttractionResponse getAttractionById(Long id) {
        Attraction attraction = getAttractionEntityById(id);
        return AttractionResponse.fromEntity(attraction);
    }

    @Override
    public Attraction getAttractionEntityById(Long id) {
        return attractionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("景点不存在，ID: " + id));
    }

    @Override
    public AttractionResponse createAttraction(AttractionRequest attractionRequest) {
        Attraction attraction = new Attraction();
        updateAttractionFromRequest(attraction, attractionRequest);

        // 设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        attraction.setCreatedTime(now);
        attraction.setUpdatedTime(now);

        Attraction savedAttraction = attractionRepository.save(attraction);
        return AttractionResponse.fromEntity(savedAttraction);
    }

    @Override
    public AttractionResponse updateAttraction(Long id, AttractionRequest attractionRequest) {
        Attraction attraction = attractionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("景点不存在，ID: " + id));

        updateAttractionFromRequest(attraction, attractionRequest);

        // 更新时间
        attraction.setUpdatedTime(LocalDateTime.now());

        Attraction updatedAttraction = attractionRepository.save(attraction);
        return AttractionResponse.fromEntity(updatedAttraction);
    }

    @Override
    public void deleteAttraction(Long id) {
        Attraction attraction = attractionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("景点不存在，ID: " + id));
        attractionRepository.delete(attraction);
    }

    @Override
    public Page<AttractionSummary> searchAttractions(String keyword, Pageable pageable) {
        return attractionRepository.searchByKeyword(keyword, pageable)
                .map(AttractionSummary::fromEntity);
    }

    @Override
    public List<AttractionSummary> getFeaturedAttractions() {
        return attractionRepository.findByIsFeaturedTrue().stream()
                .map(AttractionSummary::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Page<AttractionSummary> getTopRatedAttractions(Pageable pageable) {
        return attractionRepository.findTopRated(pageable)
                .map(AttractionSummary::fromEntity);
    }

    @Override
    public Page<AttractionSummary> getMostViewedAttractions(Pageable pageable) {
        return attractionRepository.findMostViewed(pageable)
                .map(AttractionSummary::fromEntity);
    }

    @Override
    public void incrementViewCount(Long id) {
        attractionRepository.incrementViewCount(id);
    }

    // 辅助方法：从请求DTO更新实体
    private void updateAttractionFromRequest(Attraction attraction, AttractionRequest request) {
        attraction.setProvince(request.getProvince());
        attraction.setName(request.getName());
        attraction.setStarLevel(request.getStarLevel());
        attraction.setRating(request.getRating());
        attraction.setPrice(request.getPrice());
        attraction.setSalesVolume(request.getSalesVolume());
        attraction.setRegion(request.getRegion());
        attraction.setCoordinates(request.getCoordinates());
        attraction.setDescription(request.getDescription());
        attraction.setIsFree(request.getIsFree());
        attraction.setAddress(request.getAddress());
        attraction.setRecommendedPlayTime(request.getRecommendedPlayTime());

        if (request.getTags() != null) {
            attraction.setTags(request.getTags());
        }

        if (request.getImages() != null) {
            attraction.setImages(request.getImages());
        }

        // 兼容旧代码
        if (request.getRating() != null) {
            attraction.setAverageRating(request.getRating());
        }
    }
}