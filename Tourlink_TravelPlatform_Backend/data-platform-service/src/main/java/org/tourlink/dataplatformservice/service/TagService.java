package org.tourlink.dataplatformservice.service;

import org.tourlink.dataplatformservice.entity.UserBehaviorLog;

import java.util.List;

public interface TagService {

    List<String> getTagsByTarget(UserBehaviorLog.TargetType targetType, Long targetId);

}
