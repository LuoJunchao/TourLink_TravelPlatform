package org.tourlink.dataplatformservice.schedule;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.tourlink.dataplatformservice.entity.UserProfile;
import org.tourlink.dataplatformservice.repository.UserProfileRepository;
import org.tourlink.dataplatformservice.util.UserProfileWeightUtil;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserProfileDecayScheduler {

    private final UserProfileRepository userProfileRepository;

    /**
     * 每天凌晨 2 点执行
     */
    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void decayAllUserProfiles() {
        log.info("开始执行用户画像权重衰减任务");

        // 一次性全部拉取，可根据数据量优化为分页
        userProfileRepository.findAll().forEach((this::decayUserProfile));

        log.info("用户画像权重衰减任务完成");
    }

    private void decayUserProfile(UserProfile userProfile) {
        Map<String, Double> weights = userProfile.getTagWeights();
        Map<String, LocalDateTime> updateTimes = userProfile.getTagUpdateTimes();

        if (weights == null || updateTimes == null) return;

        Map<String, Double> newWeights = new HashMap<>();
        Map<String, LocalDateTime> newUpdateTimes = new HashMap<>();

        LocalDateTime lastUpdateTime = LocalDateTime.now();

        weights.forEach((tag, oldWeight) -> {
            LocalDateTime lastUpdate = updateTimes.get(tag);
            double decayedWeight = UserProfileWeightUtil.applyTimeDecay(oldWeight, lastUpdate);
            if (decayedWeight > 0.001) { // 保留有效权重
                newWeights.put(tag, decayedWeight);
                newUpdateTimes.put(tag, lastUpdate);
            }
        });

        userProfile.setTagWeights(newWeights);
        userProfile.setTagUpdateTimes(newUpdateTimes);

        // 更新 topTags
        updateTopTags(userProfile);

        userProfileRepository.save(userProfile);
    }

    private void updateTopTags(UserProfile userProfile) {
        if (userProfile.getTagWeights().isEmpty()) return;

        var topTags = userProfile.getTagWeights().entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .toList();

        userProfile.setTopTags(topTags);
    }
}
