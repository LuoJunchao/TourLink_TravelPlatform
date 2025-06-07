package org.tourlink.dataplatformservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tourlink.dataplatformservice.client.SocialClient;
import org.tourlink.dataplatformservice.entity.UserBehaviorLog;
import org.tourlink.dataplatformservice.service.TagService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final SocialClient socialClient;

    /**
     * 根据用户行为的目标类型与 ID 获取标签列表
     * @param targetType 用户行为目标类型
     * @param targetId 用户行为目标 ID
     * @return 标签列表
     */
    @Override
    public List<String> getTagsByTarget(UserBehaviorLog.TargetType targetType, Long targetId) {
        return switch (targetType) {
            case BLOG, ATTRACTION -> socialClient.getBlogTags(targetId);
        };
    }
}
