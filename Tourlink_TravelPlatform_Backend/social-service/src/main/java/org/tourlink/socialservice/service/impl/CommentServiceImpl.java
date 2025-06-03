package org.tourlink.socialservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.tourlink.common.dto.socialDTO.CommentRequest;
import org.tourlink.common.dto.socialDTO.CommentResponse;
import org.tourlink.socialservice.converter.CommentConverter;
import org.tourlink.socialservice.entity.Blog;
import org.tourlink.socialservice.entity.BlogComment;
import org.tourlink.socialservice.repository.BlogRepository;
import org.tourlink.socialservice.repository.CommentRepository;
import org.tourlink.socialservice.service.CommentService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final BlogRepository blogRepository;
    private final CommentRepository commentRepository;

    /**
     * 在一个 blog 下新增一条 comment
     *
     * @param request commentRequest 用于传递 comment 的内容
     * @param userId 新增评论的 user
     * @return 新增的评论的信息
     */
    @Override
    @Transactional
    public CommentResponse addComment(CommentRequest request, String userId) {

        // 1. 检查博客是否存在
        Blog blog = blogRepository.findById(request.getBlogId())
                .orElseThrow(() -> new EntityNotFoundException("博客不存在:" + request.getBlogId()));


        // 2. 保存评论
        BlogComment blogComment = new BlogComment();
        blogComment.setBlog(blog);
        blogComment.setUserId(userId);
        blogComment.setContent(request.getContent());
        blogComment.setCommentTime(LocalDateTime.now());
        commentRepository.save(blogComment);

        // 3. 更新博客的评论数
        blogRepository.incrementCommentCount(request.getBlogId());

        // 4. 返回 DTO
        return CommentConverter.toResponse(blogComment);
    }

    /**
     * 分页获取 blog 下所有 comment
     *
     * @param blogId 指定的 blog
     * @param page 分页的页号
     * @param size 每页的大小
     * @return 分好页的 comments
     */
    @Override
    public Page<CommentResponse> getCommentsByBlog(Long blogId, int page, int size) {

        // 1. 检查 blog 是否存在
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new EntityNotFoundException("博客不存在:" + blogId));

        // 2. 构造分页条件(页码从0开始)
        Pageable pageable = PageRequest.of(
                page,   // 当前页(从0开始)
                size,   // 每页条数
                Sort.by("commentTime").descending()  // 按创建时间倒序
        );

        // 3. 调用 Repository 分页查询，只查询顶级评论(parentComment 为 null)
        Page<BlogComment> comments = commentRepository.findByBlog(blog, pageable);

        // 4. 将 Entity 分页对象转换为 DTO 分页对象
        return comments.map(CommentConverter::toResponse);
    }

    @Override
    public Integer getCommentCount(Long blogId) {
        return 0;
    }

    @Override
    public void deleteComment(Long commentId, String userId) {
    }
}
