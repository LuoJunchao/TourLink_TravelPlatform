package org.tourlink.socialservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tourlink.socialservice.dto.CommentRequest;
import org.tourlink.socialservice.dto.CommentResponse;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    // 添加评论
    @PostMapping
    public ResponseEntity<Void> addComment(@RequestBody CommentRequest request) {
        return null;
    }

    // 获取博客下所有评论
    @GetMapping("/blog/{blogId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByBlog(@PathVariable Long blogId) {
        return null;
    }
}

