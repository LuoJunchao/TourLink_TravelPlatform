package org.tourlink.socialservice.service;

import org.tourlink.socialservice.dto.LikeResponse;

public interface LikeService {
    LikeResponse like(Long blogId, String userId);
    LikeResponse unlike(Long blogId, String userId);
    Integer getLikeCount(Long blogId);
}
