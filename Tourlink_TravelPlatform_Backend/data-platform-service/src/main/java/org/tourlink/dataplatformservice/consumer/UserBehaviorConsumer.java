package org.tourlink.dataplatformservice.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;
import org.tourlink.common.dto.dataPlatformDTO.UserBehaviorMessage;
import org.tourlink.dataplatformservice.entity.UserBehaviorLog;
import org.tourlink.dataplatformservice.repository.UserBehaviorLogRepository;
import org.tourlink.dataplatformservice.service.UserProfileService;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class UserBehaviorConsumer {

    private final UserBehaviorLogRepository userBehaviorLogRepository;
    private final UserProfileService userProfileService;

    @RabbitListener(queues = "behavior.queue")
    public void handleUserBehavior(UserBehaviorMessage message) {
        log.info("接收到用户行为消息：{}", message);

        // 1. 转换为实体类
        UserBehaviorLog logEntity = new UserBehaviorLog();
        logEntity.setUserId(message.getUserId());
        logEntity.setTargetType(UserBehaviorLog.TargetType.valueOf(message.getTargetType()));
        logEntity.setTargetId(message.getTargetId());
        logEntity.setBehaviorType(UserBehaviorLog.BehaviorType.valueOf(message.getBehaviorType()));
        logEntity.setTimestamp(message.getTimestamp());

        // 2. 保存到数据库
        userBehaviorLogRepository.save(logEntity);
        log.info("用户行为已持久化入库：{}", logEntity);

        // 3. 实时更新用户画像
        try {
            userProfileService.updateUserProfile(logEntity);
            log.info("已更新用户画像， userId = {}", logEntity.getUserId());
        } catch (Exception e) {
            log.error("更新用户画像失败， userId = {}", logEntity.getUserId(), e);
        }
    }

}
