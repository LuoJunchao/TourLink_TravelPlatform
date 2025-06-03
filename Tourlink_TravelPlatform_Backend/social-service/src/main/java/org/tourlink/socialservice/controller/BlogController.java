package org.tourlink.socialservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tourlink.common.dto.socialDTO.BlogRequest;
import org.tourlink.common.dto.socialDTO.BlogResponse;
import org.tourlink.common.dto.socialDTO.BlogSummary;
import org.tourlink.socialservice.entity.Blog;
import org.tourlink.socialservice.repository.BlogRepository;
import org.tourlink.socialservice.service.BlogService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/social/blogs")
public class BlogController {

    private final BlogService blogService;
    private final BlogRepository blogRepository;

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello from social-service!";
    }

    // 发布博客
    @PostMapping
    public ResponseEntity<BlogResponse> publishBlog(@RequestBody @Valid BlogRequest request) {
        return ResponseEntity.ok(blogService.publishBlog(request));
    }

    // 获取博客详情
    @GetMapping("/{blogId}")
    public ResponseEntity<BlogResponse> getBlog(@PathVariable Long blogId) {
        return ResponseEntity.ok(blogService.getBlog(blogId));
    }

    // 搜索博客（按标题/内容/标签匹配）
    @GetMapping("/search")
    public ResponseEntity<Page<BlogSummary>> searchBlogs(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "title") String searchType,
            @PageableDefault(sort = "publishTime", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(blogService.searchBlogs(keyword, searchType, pageable));
    }

    // 获取某用户的博客列表
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BlogSummary>> getUserBlogs(@PathVariable String userId) {
        return ResponseEntity.ok(blogService.getUserBlogs(userId));
    }

    // 获取推荐博客列表（根据个性推荐）
    @GetMapping("/recommend")
    public ResponseEntity<Page<BlogSummary>> getRecommendedBlogs(
            @RequestParam String userId,
            @RequestParam(defaultValue = "hot") String strategy,
            @PageableDefault() Pageable pageable) {
        return ResponseEntity.ok(blogService.getRecommendedBlogs(userId, strategy, pageable));
    }

    // 获取排行榜（按热度/时间/点赞等）
    @GetMapping("/ranking")
    public ResponseEntity<Page<BlogSummary>> getBlogRanking(
            @RequestParam(defaultValue = "hot") String sortBy,
            @RequestParam(defaultValue = "all") String timeRange,
            @PageableDefault() Pageable pageable) {
        return ResponseEntity.ok(blogService.getBlogRanking(sortBy, timeRange, pageable));
    }

    // 根据博客ID返回标签列表
    @GetMapping("/{blogId}/tags")
    public List<String> getBlogTags(@PathVariable Long blogId) {
        return blogRepository.findById(blogId)
                .map(Blog::getCachedTags)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
    }

}
