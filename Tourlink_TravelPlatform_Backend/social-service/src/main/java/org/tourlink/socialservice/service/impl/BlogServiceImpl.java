package org.tourlink.socialservice.service.impl;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.tourlink.common.dto.attractionDTO.AttractionTagsDTO;
import org.tourlink.common.dto.socialDTO.BlogRequest;
import org.tourlink.common.dto.socialDTO.BlogResponse;
import org.tourlink.common.dto.socialDTO.BlogSummary;
import org.tourlink.common.response.ApiResponse;
import org.tourlink.socialservice.client.AttractionClient;
import org.tourlink.socialservice.converter.BlogConverter;
import org.tourlink.socialservice.entity.Blog;
import org.tourlink.socialservice.repository.BlogRepository;
import org.tourlink.socialservice.service.BlogService;

import java.time.LocalDateTime;
import java.util.*;

import java.util.stream.Collectors;

@Slf4j
@Service@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private static final String DEFAULT_SORT = "hot";
    private static final String DEFAULT_TIME_RANGE = "all";

    private final BlogRepository blogRepository;
    private final AttractionClient attractionClient;

    /**
     * 发布博客
     * @param request 博客请求 DTO
     * @return 博客响应 DTO
     */
    @Override
    @Transactional
    public BlogResponse publishBlog(BlogRequest request) {

        // 1. 新建博客并设置初始值
        Blog blog = new Blog();
        blog.setUserId(request.getUserId());
        blog.setTitle(request.getTitle());
        blog.setContent(request.getContent());
        blog.setImages(request.getImages() != null ? request.getImages() : new ArrayList<>());
        blog.setPublishTime(LocalDateTime.now());
        blog.setLikeCount(0);
        blog.setCommentCount(0);
        blog.setViewCount(0);

        // 2. 设置关联景点 ID （request 中包含）
        if (request.getAttractionIds() != null && !request.getAttractionIds().isEmpty()) {
            blog.setAttractionIds(request.getAttractionIds());
        }

        // 3. 先保存博客（防止 TransientPropertyValueException）
        blogRepository.save(blog);

        // 4. 根据关联景点 ID 同步更新博客缓存标签
        updateCachedTagsByAttractions(blog);

        // 5. 保存更新后的博客
        blogRepository.save(blog);

        // 6. 返回 DTO
        return BlogConverter.toResponse(blog);

    }

    /**
     *根据博客的 attractionIds 调用景点服务接口，获取标签列表合并去重，更新博客 cachedTags 字段
     * @param blog 待更新 cachedTags 的博客
     */
    private void updateCachedTagsByAttractions(Blog blog) {
        if (blog.getAttractionIds() == null || blog.getAttractionIds().isEmpty()) {
            return;
        }

        Set<String> tagSet = new HashSet<>();
        for (Long attractionId : blog.getAttractionIds()) {
            log.info("Fetching tags for attraction: {}", attractionId);
            ApiResponse<AttractionTagsDTO> response = attractionClient.getAttractionTags(attractionId);
            log.info("Response: {}", response);

            if (response != null && response.getData() != null) {
                List<String> rawTags = response.getData().getTags();
                if (rawTags != null && !rawTags.isEmpty()) {
                    for (String rawTag : rawTags) {
                        String[] splitTags = rawTag.split("\\s+");
                        tagSet.addAll(Arrays.asList(splitTags));
                    }
                }
            }
        }
        blog.setCachedTags(new ArrayList<>(tagSet));
    }

    /**
     * 获取博客详情
     * @param blogId 博客 ID
     * @return 博客响应 DTO
     */
    @Override
    @Cacheable(value = "blogDetail", key = "#blogId")
    public BlogResponse getBlog(Long blogId) {

        // 1. 检查 blog 是否存在
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new EntityNotFoundException("博客不存在:" + blogId));

        // 2. 返回 DTO
        return BlogConverter.toResponse(blog);

    }

    /**
     * 获取用户博客列表
     * @param userId 用户 ID
     * @return 博客摘要列表（按发布时间降序）
     */
    @Override
    @Cacheable(value = "userBlogs", key = "#userId")
    public List<BlogSummary> getUserBlogs(String userId) {

        // 1. 检验 userId
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("用户 ID 不能为空");
        }

        // 2. 根据 userId 查询所有博客
        List<Blog> blogs = blogRepository.findByUserIdOrderByPublishTime(userId);

        // 3. 转换为 BlogSummary 列表
        return blogs.stream()
                .map(BlogConverter::toSummary)
                .collect(Collectors.toList());
    }

    /**
     * 搜索博客
     * @param keyword 关键词
     * @param searchType 搜索类型 (title/content)
     * @param pageable 分页参数
     * @return 分页的博客摘要
     */
    @Override
    public Page<BlogSummary> searchBlogs(String keyword, String searchType, Pageable pageable) {

        // 1. 检验 keyword
        if (keyword == null || keyword.isEmpty()) {
            throw new IllegalArgumentException("搜索关键词不能为空");
        }

        // 2. 构建搜索条件
        Specification<Blog> spec = buildSearchSpecification(keyword, searchType);

        return blogRepository.findAll(spec, pageable)
                .map(BlogConverter::toSummary);

    }

    @Override
    public Page<BlogSummary> getRecommendedBlogs(String userId, String strategy, Pageable pageable) {
        return null;
    }

    /**
     * 获取博客排行榜
     * @param sortBy 排序方式 (hot/view/like/comment/new)
     * @param timeRange 时间范围 (all/day/week/month/year)
     * @param pageable 分页参数
     * @return 分页的博客摘要
     */
    @Override
    @Cacheable(value = "blogRanking", key = "{#sortBy, #timeRange, #pageable.pageNumber, #pageable.pageSize}")
    public Page<BlogSummary> getBlogRanking(String sortBy, String timeRange, Pageable pageable) {

        // 1. 使用默认值如果参数为空
        String effectiveSortBy = StringUtils.isNotBlank(sortBy) ? sortBy : DEFAULT_SORT;
        String effectiveTimeRange = StringUtils.isNotBlank(timeRange) ? timeRange : DEFAULT_TIME_RANGE;

        // 2. 构建时间范围条件
        Specification<Blog> timeSpec = buildTimeRangeSpecification(effectiveTimeRange);

        // 3. 构建排序 Pageable
        Pageable sortedPageable = buildSortedPageable(pageable, effectiveSortBy);

        // 4. 执行查询
        return blogRepository.findAll(timeSpec, sortedPageable)
                .map(BlogConverter::toSummary);
    }

    @Override
    public List<BlogResponse> getBlogs(List<Long> blogIds) {

        List<Blog> blogs = blogRepository.findAllById(blogIds);

        // 1. 转换为 BlogResponse 列表
        return blogs.stream()
                .map(BlogConverter::toResponse)
                .collect(Collectors.toList());

    }

    private Specification<Blog> buildSearchSpecification(String keyword, String searchType) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            String pattern = "%" + keyword + "%";

            if (searchType.equalsIgnoreCase("content")) {
                predicates.add(criteriaBuilder.like(root.get("content"), "%" + keyword + "%"));
            } else {
                predicates.add(criteriaBuilder.like(root.get("title"), pattern));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Specification<Blog> buildTimeRangeSpecification(String timeRange) {
        LocalDateTime startTime = getStartTimeByRange(timeRange);
        if (startTime == null) {
            return Specification.where(null);
        }

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("publishTime"), startTime);
    }

    private LocalDateTime getStartTimeByRange(String timeRange) {
        LocalDateTime now = LocalDateTime.now();
        return switch (timeRange.toLowerCase()) {
            case "day" -> now.minusDays(1); // 过去24小时
            case "week" -> now.minusWeeks(1); // 过去7天
            case "month" -> now.minusMonths(1); // 过去30天
            case "year" -> now.minusYears(1); // 过去1年
            default -> null; // 全部时间
        };
    }

    private Pageable buildSortedPageable(Pageable pageable, String sortBy) {
        List<Sort.Order> orders = Arrays.stream(sortBy.split(","))
                .map(this::convertToSortOrder)
                .collect(Collectors.toList());

        //  1. 确保结果稳定性
        if (orders.stream().noneMatch(o -> "publishTime".equals(o.getProperty()))) {
            orders.add(convertToSortOrder("publishTime"));
        }

        return PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(orders)
        );
    }

    private Sort.Order convertToSortOrder(String sortField) {
        return switch (sortField.toLowerCase()) {
            case "like" -> Sort.Order.desc("likeCount");
            case "comment" -> Sort.Order.desc("commentCount");
            case "new" -> Sort.Order.desc("publishTime");
            default ->
                Sort.Order.desc("viewCount");
        };
    }

}
