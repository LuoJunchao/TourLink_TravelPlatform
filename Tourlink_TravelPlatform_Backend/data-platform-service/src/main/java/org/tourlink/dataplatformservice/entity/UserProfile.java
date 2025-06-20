package org.tourlink.dataplatformservice.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;
import org.tourlink.common.converter.StringListJsonConverter;
import org.tourlink.common.converter.StringDoubleMapJsonConverter;
import org.tourlink.common.converter.StringLocalDateTimeMapJsonConverter;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Entity
@Table(name = "user_profile")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "location")
    private String location;

    /**
     * 用户偏好的前几个标签，例如 ["自然风光", "文化遗产"]
     */
    @Convert(converter = StringListJsonConverter.class)
    @Column(name = "top_tags")
    private List<String> topTags;

    /**
     * 所有标签的权重分布，用于个性化推荐。
     * 示例：{"自然风光": 0.8, "文化遗产": 0.3}
     */
    @Convert(converter = StringDoubleMapJsonConverter.class)
    @Column(name = "tag_weights")
    private Map<String, Double> tagWeights;

    /**
     * 每个标签更新的最后时间
     * 示例：{"自然风光": "2025-06-01T00:00:00", "文化遗产": "2025-06-05T12:34:56"}
     */
    @Lob
    @Convert(converter = StringLocalDateTimeMapJsonConverter.class)
    @Column(name = "tag_update_times")
    private Map<String, LocalDateTime> tagUpdateTimes;

    @UpdateTimestamp
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

}