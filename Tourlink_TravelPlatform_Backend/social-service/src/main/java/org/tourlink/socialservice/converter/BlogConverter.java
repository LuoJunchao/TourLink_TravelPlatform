package org.tourlink.socialservice.converter;

import org.tourlink.common.dto.socialDTO.BlogResponse;
import org.tourlink.common.dto.socialDTO.BlogSummary;
import org.tourlink.socialservice.entity.Blog;

public class BlogConverter {
    public static BlogResponse toResponse(Blog blog) {

        BlogResponse blogResponse = new BlogResponse();

        blogResponse.setBlogId(blog.getBlogId());
        blogResponse.setUserId(blog.getUserId());
        blogResponse.setTitle(blog.getTitle());
        blogResponse.setContent(blog.getContent());
        blogResponse.setPublishTime(blog.getPublishTime());
        blogResponse.setTags(blog.getCachedTags());
        blogResponse.setViewCount(blog.getViewCount());
        blogResponse.setLikeCount(blog.getLikeCount());
        blogResponse.setCommentCount(blog.getCommentCount());

        return blogResponse;
    }

    public static BlogSummary toSummary(Blog blog) {
        BlogSummary summary = new BlogSummary();

        summary.setBlogId(blog.getBlogId());
        summary.setTitle(blog.getTitle());
        summary.setUserId(blog.getUserId());
        summary.setPublishTime(blog.getPublishTime());
        summary.setLikeCount(blog.getLikeCount());
        summary.setViewCount(blog.getViewCount());
        summary.setCommentCount(blog.getCommentCount());

        // 安全处理封面图
        if (blog.getImages() != null && !blog.getImages().isEmpty()) {
            summary.setCoverImage(blog.getImages().get(0));
        } else {
            summary.setCoverImage(null); // 或者 ""
        }

        return summary;
    }
}
