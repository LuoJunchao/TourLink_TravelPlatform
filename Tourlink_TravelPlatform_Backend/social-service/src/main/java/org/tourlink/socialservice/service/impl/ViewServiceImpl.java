package org.tourlink.socialservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tourlink.common.dto.dataPlatformDTO.UserBehaviorMessage;
import org.tourlink.common.dto.socialDTO.ViewResponse;
import org.tourlink.socialservice.entity.Blog;
import org.tourlink.socialservice.entity.BlogView;
import org.tourlink.socialservice.repository.BlogRepository;
import org.tourlink.socialservice.repository.ViewRepository;
import org.tourlink.socialservice.service.ViewService;
import org.tourlink.socialservice.service.event.BehaviorEventSender;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ViewServiceImpl implements ViewService {

    private final ViewRepository viewRepository;
    private final BlogRepository blogRepository;
    private final BehaviorEventSender behaviorEventSender;

    /**
     * 用户浏览博客时记录浏览信息
     * 如果该用户是第一次查看该博客，则记录一次新浏览，否则不重复计数
     * @param blogId 博客 ID
     * @param userId 用户 ID
     * @return 浏览响应信息，包括是否首次浏览及总浏览量
     */
    @Override
    public ViewResponse view(Long blogId, String userId) {

        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new EntityNotFoundException("博客不存在:" + blogId));

        boolean isFirstView = !viewRepository.existsByBlogAndUserId(blog, userId);

        if (isFirstView) {
            BlogView view = new BlogView();
            view.setBlog(blog);
            view.setUserId(userId);
            view.setViewTime(LocalDateTime.now());
            viewRepository.save(view);

            blogRepository.incrementViewCount(blogId);
        }

        int totalViews = blog.getViewCount();

        // 发送行为消息
        UserBehaviorMessage message = new UserBehaviorMessage(
                userId,
                "BLOG",
                blogId,
                "VIEW",
                LocalDateTime.now()
        );
        behaviorEventSender.send(message);

        return ViewResponse.builder()
                .totalViews(totalViews)
                .isFirstView(isFirstView)
                .blogId(blogId)
                .userId(userId)
                .viewTime(LocalDateTime.now())
                .build();
    }

    /**
     * 获取指定博客的总浏览次数
     * @param blogId 博客 ID
     * @return 总浏览量
     */
    @Override
    public Integer getViewCount(Long blogId) {

        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new EntityNotFoundException("博客不存在:" + blogId));

        return blog.getViewCount();
    }
}
