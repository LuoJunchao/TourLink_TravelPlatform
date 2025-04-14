package org.tourlink.socialservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    // 点赞
    @PostMapping
    public ResponseEntity<Void> likeBlog(@RequestParam Long blogId, @RequestParam String userId) {
        return null;
    }

    // 取消点赞
    @DeleteMapping
    public ResponseEntity<Void> unlikeBlog(@RequestParam Long blogId, @RequestParam String userId) {
        return null;
    }

    // 查询博客的点赞数
    @GetMapping("/count")
    public ResponseEntity<Integer> getLikeCount(@RequestParam Long blogId) {
        return null;
    }

}

