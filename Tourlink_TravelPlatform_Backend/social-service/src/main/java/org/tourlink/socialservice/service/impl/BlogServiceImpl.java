package org.tourlink.socialservice.service.impl;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.tourlink.socialservice.dto.BlogRequest;
import org.tourlink.socialservice.dto.BlogResponse;
import org.tourlink.socialservice.dto.BlogSummary;
import org.tourlink.socialservice.entity.Blog;
import org.tourlink.socialservice.entity.BlogTag;
import org.tourlink.socialservice.entity.Tag;
import org.tourlink.socialservice.repository.BlogRepository;
import org.tourlink.socialservice.repository.TagRepository;
import org.tourlink.socialservice.service.BlogService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private static final String DEFAULT_SORT = "hot";
    private static final String DEFAULT_TIME_RANGE = "all";
    private static final int MAX_TAG_LENGTH = 20;

    private final BlogRepository blogRepository;
    private final TagRepository tagRepository;

    /**
     * 发布博客
     *
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
        blog.setHotScore(0.0);

        // 2. 先保存博客（防止 TransientPropertyValueException）
        blogRepository.save(blog);

        // 3. 处理标签
        processBlogTags(blog, request.getTags());

        // 4. 返回 DTO
        return BlogResponse.convertToResponse(blog);

    }

    /**
     * 获取博客详情
     *
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
        return BlogResponse.convertToResponse(blog);

    }

    /**
     * 获取用户博客列表
     *
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
                .map(BlogSummary::convertToSummary)
                .collect(Collectors.toList());
    }

    /**
     * 搜索博客
     *
     * @param keyword 关键词
     * @param searchType 搜索类型 (title/content/tag)
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
                .map(BlogSummary::convertToSummary);

    }

    @Override
    public Page<BlogSummary> getRecommendedBlogs(String userId, String strategy, Pageable pageable) {
        return null;
    }

    /**
     * 获取博客排行榜
     *
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
                .map(BlogSummary::convertToSummary);
    }

    private void processBlogTags(Blog blog, List<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) {
            return;
        }

        tagNames.stream()
                .map(this::cleanTagName)
                .forEach(tagName -> {
                    Tag tag = tagRepository.findByName(tagName)
                            .orElseGet(() -> tagRepository.save(new Tag(tagName)));
                    blog.addTag(tag);
                    tag.setUsageCount(tag.getUsageCount() + 1);
                });
    }

    private String cleanTagName(String tagName) {
        if (tagName.length() > MAX_TAG_LENGTH) {
            return tagName.substring(0, MAX_TAG_LENGTH);
        }
        return tagName;
    }

    private Specification<Blog> buildSearchSpecification(String keyword, String searchType) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            String pattern = "%" + keyword + "%";

            switch (searchType.toLowerCase()) {
                case "content":
                    predicates.add(criteriaBuilder.like(root.get("content"), "%" + keyword + "%"));
                    break;
                case "tag":
                    Join<Blog, BlogTag> blogTagJoin = root.join("blogTags", JoinType.INNER);
                    Join<BlogTag, Tag> tagJoin = blogTagJoin.join("tag", JoinType.INNER);
                    predicates.add(criteriaBuilder.like(tagJoin.get("name"), "%" + keyword + "%"));
                    break;
                default: // title
                    predicates.add(criteriaBuilder.like(root.get("title"),pattern));
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

        //  3. 确保结果稳定性
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
            case "view" -> Sort.Order.desc("viewCount");
            case "like" -> Sort.Order.desc("likeCount");
            case "comment" -> Sort.Order.desc("commentCount");
            case "new" -> Sort.Order.desc("publishTime");
            default -> // hot
                    Sort.Order.desc("hotScore");
        };
    }
}
