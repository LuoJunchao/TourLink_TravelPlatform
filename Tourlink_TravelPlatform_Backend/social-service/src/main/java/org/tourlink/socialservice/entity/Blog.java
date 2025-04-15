package org.tourlink.socialservice.entity;

import com.alibaba.nacos.shaded.com.google.gson.Gson;
import com.alibaba.nacos.shaded.com.google.gson.reflect.TypeToken;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
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
    @Convert(converter = StringListConverter.class)
    @Column(name = "images", columnDefinition = "JSON")
    private List<String> images;

    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL)
    private List<BlogTag> blogTags;

    @Column(name = "publish_time", nullable = false)
    private LocalDateTime publishTime;

    @Column(name = "view_count", columnDefinition = "INT DEFAULT 0")
    private Integer viewCount = 0;

    @Column(name = "hot_score", columnDefinition = "DOUBLE DEFAULT 0")
    private Double hotScore = 0.0; // 推荐权重（博客热度）

}

// JSON列表转换器
@Converter
class StringListConverter implements AttributeConverter<List<String>, String> {
    @Override
    public String convertToDatabaseColumn(List<String> list) {
        return new Gson().toJson(list);
    }

    @Override
    public List<String> convertToEntityAttribute(String json) {
        return new Gson().fromJson(json, new TypeToken<List<String>>(){}.getType());
    }

}
