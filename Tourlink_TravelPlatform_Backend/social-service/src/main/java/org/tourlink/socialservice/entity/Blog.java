package org.tourlink.socialservice.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.tourlink.common.converter.StringListJsonConverter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "blog")
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blog_id")
    private Long blogId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "title")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    // 图片 URL 列表使用 @Convert 存储为 JSON 字符串
    @Convert(converter = StringListJsonConverter.class)
    @Column(name = "images", columnDefinition = "JSON")
    private List<String> images;

    // 关联的景点 ID 列表（存储为 JSON 数组）
    @Convert(converter = StringListJsonConverter.class)
    @Column(name = "attraction_ids", columnDefinition = "JSON")
    private List<Long> attractionIds;

    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BlogTag> blogTags = new ArrayList<>();

    @Column(name = "publish_time", nullable = false)
    private LocalDateTime publishTime;

    @Column(name = "like_count", columnDefinition = "INT DEFAULT 0")
    private Integer likeCount = 0;

    @Column(name = "comment_count", columnDefinition = "INT DEFAULT 0")
    private Integer commentCount = 0;

    @Column(name = "view_count", columnDefinition = "INT DEFAULT 0")
    private Integer viewCount = 0;

    @Column(name = "share_count", columnDefinition = "INT DEFAULT 0")
    private Integer shareCount = 0;

    @Column(name = "hot_score", columnDefinition = "DOUBLE DEFAULT 0")
    private Double hotScore = 0.0; // 推荐权重（博客热度）

    // 添加标签
    public void addTag(Tag tag) {
        BlogTag blogTag = new BlogTag(this, tag);
        blogTags.add(blogTag);
        tag.getBlogTags().add(blogTag);
    }

}

