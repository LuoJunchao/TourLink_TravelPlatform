package org.tourlink.dataplatformservice.constant;

import lombok.Getter;
import org.tourlink.dataplatformservice.entity.UserBehaviorLog;

@Getter
public enum BehaviorWeight {

    VIEW(0.05),
    LIKE(0.1),
    COMMENT(0.2),
    COLLECT(0.3);

    private final double weight;

    BehaviorWeight(double weight) {
        this.weight = weight;
    }

    // 根据行为类型获取对应权重，默认0
    public static double getWeightByBehaviorType(UserBehaviorLog.BehaviorType behaviorType) {
        if (behaviorType == null) return 0.0;
        try {
            return BehaviorWeight.valueOf(behaviorType.name()).getWeight();
        } catch (IllegalArgumentException e) {
            return 0.0;
        }
    }

}
