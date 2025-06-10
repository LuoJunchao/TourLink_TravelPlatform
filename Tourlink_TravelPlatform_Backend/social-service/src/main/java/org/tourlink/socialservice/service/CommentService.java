package org.tourlink.socialservice.service;

import org.springframework.data.domain.Page;
import org.tourlink.common.dto.socialDTO.CommentRequest;
import org.tourlink.common.dto.socialDTO.CommentResponse;

public interface CommentService {

    CommentResponse addComment(CommentRequest request, String userId);

    Page<CommentResponse> getCommentsByBlog(Long blogId, int page, int size);

    Integer getCommentCount(Long blogId);

    void deleteComment(Long commentId, String userId);
}
