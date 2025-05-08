package org.tourlink.socialservice.service;

import org.springframework.data.domain.Page;
import org.tourlink.socialservice.dto.CommentRequest;
import org.tourlink.socialservice.dto.CommentResponse;

public interface CommentService {

    CommentResponse addComment(CommentRequest request, String userId);

    Page<CommentResponse> getCommentsByBlog(Long blogId, int page, int size);

    Integer getCommentCount(Long blogId);

    void deleteComment(Long commentId, String userId);
}
