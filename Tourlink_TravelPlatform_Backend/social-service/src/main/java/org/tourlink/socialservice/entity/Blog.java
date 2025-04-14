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

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Convert(converter = StringListConverter.class)
    @Column(name = "images", columnDefinition = "JSON")
    private List<String> images;

    @Convert(converter = StringListConverter.class)
    @Column(name = "tags", columnDefinition = "JSON")
    private List<String> tags;

    @Column(name = "publish_time", nullable = false)
    private LocalDateTime publishTime;

    @Column(name = "view_count", columnDefinition = "INT DEFAULT 0")
    private Integer viewCount = 0;
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
