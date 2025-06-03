package org.tourlink.dataplatformservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tourlink.dataplatformservice.entity.UserBehaviorLog;
import org.tourlink.dataplatformservice.entity.UserProfile;
import org.tourlink.dataplatformservice.repository.UserProfileRepository;
import org.tourlink.dataplatformservice.service.TagService;
import org.tourlink.dataplatformservice.service.UserProfileService;
import org.tourlink.dataplatformservice.util.UserProfileWeightUtil;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final TagService tagService;
    private final UserProfileRepository userProfileRepository;

    @Transactional
    @Override
    public void updateUserProfile(UserBehaviorLog behaviorLog) {

        // 1. 获取关联标签
        List<String> tags = tagService.getTagsByTarget(
                behaviorLog.getTargetType(),
                behaviorLog.getTargetId()
        );
        if (tags.isEmpty()) return;

        // 2. 获取或创建用户画像
        UserProfile profile = userProfileRepository.findByUserId(behaviorLog.getUserId())
                .orElseGet(() -> createNewProfile(behaviorLog.getUserId()));

        // 3. 更新权重
        Map<String, Double> updatedWeights = updateTagWeights(
                profile.getTagWeights(),
                tags,
                behaviorLog.getBehaviorType(),
                behaviorLog.getTimestamp()
        );

        // 4. 保存更新
        profile.setTagWeights(updatedWeights);
        updateTopTags(profile);
        userProfileRepository.save(profile);
    }

    private UserProfile createNewProfile(String userId) {
        UserProfile profile = new UserProfile();
        profile.setUserId(userId);
        profile.setTagWeights(new HashMap<>());
        return profile;
    }

    private Map<String, Double> updateTagWeights(
            Map<String, Double> currentWeights,
            List<String> tags,
            UserBehaviorLog.BehaviorType behaviorType,
            LocalDateTime actionTime
    ) {
        Map<String, Double> weights = Optional.ofNullable(currentWeights)
                .orElseGet(HashMap::new);

        tags.forEach(tag -> {
            double oldWeight = weights.getOrDefault(tag, 0.0);
            double newWeight = UserProfileWeightUtil.updateTagWeights(
                    oldWeight,
                    behaviorType,
                    actionTime,
                    tags.size()
            );
            weights.put(tag, newWeight);
        });
        return weights;
    }

    private void updateTopTags(UserProfile profile) {
        if (profile.getTagWeights().isEmpty()) return;

        List<String> topTags = profile.getTagWeights().entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        profile.setTopTags(topTags);
    }

}
