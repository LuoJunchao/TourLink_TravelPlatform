package org.tourlink.routingservice.entity;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class UserPreference {
    private Map<String, Double> tagWeights; // 各标签偏好（需归一化）
    private List<String> selectedTags; // 主动选择的标签
}
