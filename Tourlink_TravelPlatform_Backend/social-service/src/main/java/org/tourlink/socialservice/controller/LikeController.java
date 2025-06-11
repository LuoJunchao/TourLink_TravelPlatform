package org.tourlink.socialservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tourlink.common.dto.socialDTO.LikeRequest;
import org.tourlink.common.dto.socialDTO.LikeResponse;
import org.tourlink.socialservice.service.LikeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;

    // 点赞
    @PostMapping
    public ResponseEntity<LikeResponse> likeBlog(@RequestBody LikeRequest likeRequest) {
        return ResponseEntity.ok(likeService.like(likeRequest.getBlogId(), likeRequest.getUserId()));
    }

    // 取消点赞
    @DeleteMapping
    public ResponseEntity<LikeResponse> unlikeBlog(
            @RequestParam Long blogId,
            @RequestParam String userId) {
        return ResponseEntity.ok(likeService.unlike(blogId, userId));
    }

    // 查询博客的点赞数
    @GetMapping("/count")
    public ResponseEntity<Integer> getLikeCount(
            @RequestParam Long blogId) {
        return ResponseEntity.ok(likeService.getLikeCount(blogId));
    }

}

