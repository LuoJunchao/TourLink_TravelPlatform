package org.tourlink.common.dto.socialDTO;

import lombok.Data;

@Data
public class LikeRequest {

    private Long blogId;
    private String userId;
}
