package org.tourlink.socialservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long tagId;

    @Column(name = "name", unique = true, nullable = false)
    private String name; // 标签名，如“徒步”、“丽江”

    @Column(name = "description")
    private String description; // 标签描述（可选）

    @Column(name = "icon_url")
    private String iconUrl; // 可选：用于前端展示图标

    @Column(name = "usage_count", columnDefinition = "INT DEFAULT 0")
    private Integer usageCount = 0; // 被引用次数，可用于热门标签排序

}
