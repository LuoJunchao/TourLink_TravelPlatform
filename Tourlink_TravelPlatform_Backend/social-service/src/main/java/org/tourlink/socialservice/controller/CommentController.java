package org.tourlink.socialservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tourlink.socialservice.dto.CommentRequest;
import org.tourlink.socialservice.dto.CommentResponse;
import org.tourlink.socialservice.service.CommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    // 添加评论
    @PostMapping
    public ResponseEntity<CommentResponse> addComment(
            @RequestBody @Valid CommentRequest request,
            @RequestHeader("userId") String userId) {
        return ResponseEntity.ok(commentService.addComment(request, userId));
    }

    // 分页获取博客评论
    @GetMapping("/blog/{blogId}")
    public ResponseEntity<Page<CommentResponse>> getCommentsByBlogId(
            @PathVariable Long blogId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(commentService.getCommentsByBlog(blogId, page, size));
    }

    // 删除评论
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @RequestHeader("userId") String userId
    ) {
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.noContent().build();
    }

    // 获取博客评论数
    @GetMapping("/blog/{blogId}/count")
    public ResponseEntity<Integer> getCommentCount(@PathVariable Long blogId) {
        return ResponseEntity.ok(commentService.getCommentCount(blogId));
    }

}

