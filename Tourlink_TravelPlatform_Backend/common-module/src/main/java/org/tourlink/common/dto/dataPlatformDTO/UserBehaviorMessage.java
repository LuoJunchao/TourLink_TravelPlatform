package org.tourlink.common.dto.dataPlatformDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBehaviorMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String userId;
    private String targetType;    // BLOG / ATTRACTION
    private Long targetId;
    private String behaviorType;  // VIEW / LIKE / COMMENT / COLLECT
    private LocalDateTime timestamp;

}
