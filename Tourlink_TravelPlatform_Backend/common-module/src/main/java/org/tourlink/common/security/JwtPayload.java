package org.tourlink.common.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * JWT载荷
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtPayload {
    private Long userId;
    private String username;
    private List<String> roles;
    private long expiration;
}
