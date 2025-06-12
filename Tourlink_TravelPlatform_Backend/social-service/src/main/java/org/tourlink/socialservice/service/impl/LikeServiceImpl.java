package org.tourlink.socialservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tourlink.common.dto.dataPlatformDTO.UserBehaviorMessage;
import org.tourlink.common.dto.socialDTO.LikeResponse;
import org.tourlink.socialservice.entity.Blog;
import org.tourlink.socialservice.entity.BlogLike;
import org.tourlink.socialservice.repository.BlogRepository;
import org.tourlink.socialservice.repository.LikeRepository;
import org.tourlink.socialservice.service.LikeService;
import org.tourlink.socialservice.service.event.BehaviorEventSender;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {


    private final BlogRepository blogRepository;
    private final LikeRepository likeRepository;
    private final BehaviorEventSender behaviorEventSender;

    /**
     * 用户为一个 blog 点赞
     *
     * @param blogId 点赞的 blog
     * @param userId 发起点赞的 user
     * @return 本次点赞的信息
     */
    @Transactional
    @Override
    public LikeResponse like(Long blogId, String userId) {

        // 1. 检查是否已经点过赞
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new EntityNotFoundException("博客不存在:" + blogId));
        if(likeRepository.existsByBlogAndUserId(blog, userId)) {
            throw new IllegalStateException("您已经点赞过此博客");
        }

        // 2. 创建新的 BlogLike 实体、完善信息并保存
        BlogLike blogLike = new BlogLike();
        blogLike.setBlog(blog);
        blogLike.setUserId(userId);
        blogLike.setLikeTime(LocalDateTime.now());
        likeRepository.save(blogLike);

        // 3. blog 点赞数 + 1
        blogRepository.incrementLikeCount(blogId);

        // 4. 发送行为消息
        UserBehaviorMessage message = new UserBehaviorMessage(
                userId,
                "BLOG",
                blogId,
                "LIKE",
                LocalDateTime.now()
        );
        behaviorEventSender.send(message);

        // 5. 封装为 LikeResponse 返回信息
        return LikeResponse.builder()
                .blogId(blogId)
                .userId(userId)
                .liked(true)
                .likeTime(blogLike.getLikeTime())
                .build();
    }

    /**
     * 用户取消一个 blog 点赞
     *
     * @param blogId 取消点赞的 blog
     * @param userId 取消点赞的 user
     * @return 取消点赞的信息
     */
    @Transactional
    @Override
    public LikeResponse unlike(Long blogId, String userId) {

        // 1. 根据 blogId 和 userId 确定唯一的要取消的点赞
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new EntityNotFoundException("博客不存在:" + blogId));

        BlogLike blogLike = likeRepository.findByBlogAndUserId(blog, userId);

        // 2. blog 点赞数 -1
        blogRepository.decrementLikeCount(blogId);

        // 3. 删除点赞记录
        likeRepository.delete(blogLike);

        // 4. 封装为 LikeResponse 返回信息
        return LikeResponse.builder()
                .blogId(blogId)
                .userId(userId)
                .liked(false)
                .likeTime(LocalDateTime.now())
                .build();
    }

    /**
     * 查询一个 blog 的点赞数
     *
     * @param blogId 要查询的 blog
     * @return 查询的 blog 的点赞数
     */
    @Override
    public Integer getLikeCount(Long blogId) {

        // 1. 根据 blogId 确定 blog
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new EntityNotFoundException("博客不存在:" + blogId));

        // 2. 返回 blog 的 likeCount
        return blog.getLikeCount();
    }

    @Override
    public Boolean hasLiked(Long blogId, String userId) {

        // 1. 根据 blogId 确定 blog
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new EntityNotFoundException("博客不存在:" + blogId));

        return likeRepository.existsByBlogAndUserId(blog, userId);
    }

}
