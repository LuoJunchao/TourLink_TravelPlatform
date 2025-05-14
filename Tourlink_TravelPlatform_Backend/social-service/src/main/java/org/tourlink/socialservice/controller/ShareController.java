package org.tourlink.socialservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shares")
public class ShareController {

    // 分享博客
    @PostMapping
    public ResponseEntity<Void> shareBlog(@RequestParam Long blogId, @RequestParam String userId) {
        return null;
    }

    // 获取某博客分享次数
    @GetMapping("/count")
    public ResponseEntity<Integer> getShareCount(@RequestParam Long blogId) {
        return null;
    }
}

