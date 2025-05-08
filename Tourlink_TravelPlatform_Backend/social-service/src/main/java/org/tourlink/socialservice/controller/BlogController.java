package org.tourlink.socialservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tourlink.socialservice.dto.BlogRequest;
import org.tourlink.socialservice.dto.BlogResponse;
import org.tourlink.socialservice.dto.BlogSummary;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {

    // 发布博客
    @PostMapping
    public ResponseEntity<Void> publishBlog(@RequestBody BlogRequest request) {
        return null;
    }

    // 获取博客详情
    @GetMapping("/{blogId}")
    public ResponseEntity<BlogResponse> getBlog(@PathVariable Long blogId) {
        return null;
    }

    // 获取某用户的博客列表
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BlogSummary>> getUserBlogs(@PathVariable String userId) {
        return null;
    }

    // 获取推荐博客列表（根据热度或个性推荐）
    @GetMapping("/recommend")
    public ResponseEntity<List<BlogSummary>> getRecommendedBlogs(@RequestParam String userId) {
        return null;
    }

    // 获取排行榜（按热度/时间/点赞）
    @GetMapping("/ranking")
    public ResponseEntity<List<BlogSummary>> getBlogRanking(@RequestParam(defaultValue = "hot") String sortBy) {
        return null;
    }

}
