package org.tourlink.dataplatformservice.util;

import org.tourlink.dataplatformservice.entity.UserBehaviorLog;

import java.time.Duration;
import java.time.LocalDateTime;

public class UserProfileWeightUtil {

    /**
     * 时间半衰期，单位：天，行为权重每经过半衰期衰减一半
     */
    private static final double HALF_LIFE_DAYS = 30.0;

    /**
     * 强化因子，重复行为时对旧权重的放大倍数
     */
    private static final double REINFORCEMENT_FACTOR = 1.2;

    /**
     * 权重最大值（归一化上限）
     */
    private static final double MAX_WEIGHT = 1.0;

    /**
     * 计算时间衰减因子
     * @param actionTime 行为发生时间
     * @return 衰减系数（0，1】
     */
    public static double calculateDecayFactor(LocalDateTime actionTime) {
        long daysElapsed = Duration.between(actionTime, LocalDateTime.now()).toDays();
        return Math.pow(0.5, daysElapsed / HALF_LIFE_DAYS);
    }

    public static double updateTagWeights(double oldWeight, UserBehaviorLog.BehaviorType behaviorType, LocalDateTime lastActionTime, int tagCount) {
        double baseWeight = BehaviorWeightUtil.calculateWeight(behaviorType);
        double decayFactor = calculateDecayFactor(lastActionTime);

        // 渐进增长公式：权重增量 = （基础权重 * 衰减系数） / 标签数量
        double incrementalWeight = (baseWeight * decayFactor) / tagCount * REINFORCEMENT_FACTOR;
        double newWeight = oldWeight + (1 - oldWeight) * incrementalWeight;

        return Math.min(MAX_WEIGHT, newWeight);
    }

}
