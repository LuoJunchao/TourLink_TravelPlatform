package org.tourlink.dataplatformservice.util;

import org.tourlink.dataplatformservice.constant.BehaviorWeight;
import org.tourlink.dataplatformservice.entity.UserBehaviorLog;

public class BehaviorWeightUtil {

    public static double calculateWeight(UserBehaviorLog.BehaviorType behaviorType) {
        return BehaviorWeight.getWeightByBehaviorType(behaviorType);
    }
}
