package org.tourlink.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户基本信息DTO，用于微服务间传递用户基本信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBasicDTO {
    private Long id;
    private String username;
    private String email;
    private String avatarUrl;
}
