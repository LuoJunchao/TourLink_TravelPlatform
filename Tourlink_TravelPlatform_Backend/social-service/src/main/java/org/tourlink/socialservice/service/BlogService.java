package org.tourlink.socialservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.tourlink.common.dto.socialDTO.BlogRequest;
import org.tourlink.common.dto.socialDTO.BlogResponse;
import org.tourlink.common.dto.socialDTO.BlogSummary;

import java.util.List;

public interface BlogService {

    BlogResponse publishBlog(BlogRequest request);

    BlogResponse getBlog(Long blogId);

    List<BlogSummary> getUserBlogs(String userId);

    Page<BlogSummary> searchBlogs(String keyword, String searchType, Pageable pageable);

    Page<BlogSummary> getRecommendedBlogs(String userId, String strategy, Pageable pageable);

    Page<BlogSummary> getBlogRanking(String sortBy, String timeRange, Pageable pageable);

    List<BlogSummary> getBlogs(List<Long> blogIds);
}
