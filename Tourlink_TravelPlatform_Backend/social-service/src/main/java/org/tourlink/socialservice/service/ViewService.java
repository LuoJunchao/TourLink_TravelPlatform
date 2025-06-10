package org.tourlink.socialservice.service;

import org.tourlink.common.dto.socialDTO.ViewResponse;

public interface ViewService {

    ViewResponse view(Long blogId, String userId);

    Integer getViewCount(Long blogId);
}
