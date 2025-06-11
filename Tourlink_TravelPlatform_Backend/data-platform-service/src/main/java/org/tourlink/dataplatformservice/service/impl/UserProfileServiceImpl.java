package org.tourlink.dataplatformservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tourlink.common.dto.dataPlatformDTO.UserProfileDTO;
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

        // 3. 初始化两个 Map
        Map<String, Double> weights = Optional.ofNullable(profile.getTagWeights()).orElseGet(HashMap::new);
        Map<String, LocalDateTime> updateTimes = Optional.ofNullable(profile.getTagUpdateTimes()).orElseGet(HashMap::new);

        double increment = UserProfileWeightUtil.calculateIncrement(behaviorLog.getBehaviorType(), tags.size());

        for (String tag : tags) {
            double oldWeight = weights.getOrDefault(tag, 0d);
            LocalDateTime lastUpdate = updateTimes.get(tag);

            // 衰减旧值
            double decayed = UserProfileWeightUtil.applyTimeDecay(oldWeight, lastUpdate);

            // 更新权重并刷新更新时间
            double newWeight = UserProfileWeightUtil.mergeWeight(decayed, increment);
            weights.put(tag, newWeight);
            updateTimes.put(tag, behaviorLog.getTimestamp());
        }

        profile.setTagWeights(weights);
        profile.setTagUpdateTimes(updateTimes);
        updateTopTags(profile);

        userProfileRepository.save(profile);
    }

    @Override
    public UserProfileDTO getUserProfile(String userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在！"));

        return new UserProfileDTO(
                userProfile.getUserId(),
                userProfile.getTopTags(),
                userProfile.getTagWeights(),
                userProfile.getTagUpdateTimes()
        );
    }

    private UserProfile createNewProfile(String userId) {
        UserProfile profile = new UserProfile();
        profile.setUserId(userId);
        profile.setTagWeights(new HashMap<>());
        profile.setTagUpdateTimes(new HashMap<>());
        return profile;
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
