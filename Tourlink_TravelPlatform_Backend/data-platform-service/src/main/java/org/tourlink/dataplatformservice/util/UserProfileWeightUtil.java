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

    public static double applyTimeDecay(double weight, LocalDateTime lastUpdateTime) {
        if (lastUpdateTime == null) return weight; // 如果没有更新时间，直接返回原值

        long daysElapsed = Duration.between(lastUpdateTime, LocalDateTime.now()).toDays();
        double decayFactor = Math.pow(0.5, daysElapsed/HALF_LIFE_DAYS);
        return weight * decayFactor;
    }

    public static double calculateIncrement(
            UserBehaviorLog.BehaviorType behaviorType,
            int tagCount
    ) {
        double baseWeight = BehaviorWeightUtil.calculateWeight(behaviorType);
        return (baseWeight / tagCount) * REINFORCEMENT_FACTOR;
    }

    public static double mergeWeight(double decayedWeight, double increment) {
        return Math.min(MAX_WEIGHT, decayedWeight + (1 - decayedWeight) * increment);
    }

}
