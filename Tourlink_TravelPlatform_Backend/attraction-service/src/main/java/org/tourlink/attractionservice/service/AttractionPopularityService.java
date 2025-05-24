package org.tourlink.attractionservice.service;

import org.tourlink.attractionservice.entity.AttractionPopularity;

import java.util.List;

public interface AttractionPopularityService {
    List<AttractionPopularity> getAllPopularity();

    AttractionPopularity getPopularityById(Long id);

    AttractionPopularity getPopularityByAttractionId(Long attractionId);

    AttractionPopularity createPopularity(AttractionPopularity popularity);

    AttractionPopularity updatePopularityCounts(
            Long id,
            Integer viewCount,
            Integer likeCount,
            Integer shareCount
    );

    void deletePopularityById(Long id);

    // 增加常用操作
    AttractionPopularity incrementViewCount(Long attractionId);
    AttractionPopularity incrementLikeCount(Long attractionId);
    AttractionPopularity incrementShareCount(Long attractionId);
}