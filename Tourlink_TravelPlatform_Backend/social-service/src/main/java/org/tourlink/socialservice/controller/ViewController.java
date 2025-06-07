package org.tourlink.socialservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tourlink.common.dto.socialDTO.ViewResponse;
import org.tourlink.socialservice.service.ViewService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/views")
public class ViewController {

    private final ViewService viewService;

    /**
     * 用户查看博客
     * @param blogId 博客
     * @param userId 用户
     * @return 接口调用结果
     */
    @PostMapping
    public ResponseEntity<ViewResponse> viewBlog(
            @RequestParam Long blogId,
            @RequestParam String userId) {
        return ResponseEntity.ok(viewService.view(blogId, userId));
    }

    /**
     * 查询博客的总浏览次数
     * @param blogId 博客
     * @return 接口调用结果
     */
    @GetMapping("/count")
    public ResponseEntity<Integer> getViewCount(
            @RequestParam Long blogId) {
        return ResponseEntity.ok(viewService.getViewCount(blogId));
    }
}
