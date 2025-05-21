package org.tourlink.attractionservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.tourlink.attractionservice.dto.AttractionRequest;
import org.tourlink.attractionservice.dto.AttractionResponse;
import org.tourlink.attractionservice.dto.AttractionSummary;
import org.tourlink.attractionservice.entity.Attraction;

import java.util.List;

public interface AttractionService {
    List<AttractionSummary> getAllAttractions();

    Page<AttractionSummary> getAttractionsPaged(Pageable pageable);

    AttractionResponse getAttractionById(Long id);

    // 获取Attraction实体对象，用于内部服务之间调用
    Attraction getAttractionEntityById(Long id);

    AttractionResponse createAttraction(AttractionRequest attractionRequest);

    AttractionResponse updateAttraction(Long id, AttractionRequest attractionRequest);

    void deleteAttraction(Long id);

    Page<AttractionSummary> searchAttractions(String keyword, Pageable pageable);

    List<AttractionSummary> getFeaturedAttractions();

    Page<AttractionSummary> getTopRatedAttractions(Pageable pageable);

    Page<AttractionSummary> getMostViewedAttractions(Pageable pageable);

    void incrementViewCount(Long id);
}