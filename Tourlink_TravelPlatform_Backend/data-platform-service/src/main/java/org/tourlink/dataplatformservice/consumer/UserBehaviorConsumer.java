package org.tourlink.dataplatformservice.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;
import org.tourlink.common.dto.dataPlatformDTO.UserBehaviorMessage;
import org.tourlink.dataplatformservice.entity.UserBehaviorLog;
import org.tourlink.dataplatformservice.repository.UserBehaviorLogRepository;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class UserBehaviorConsumer {

    private final UserBehaviorLogRepository userBehaviorLogRepository;

    @RabbitListener(queues = "behavior.queue")
    public void handleUserBehavior(UserBehaviorMessage message) {
        log.info("接收到用户行为消息：{}", message);

        UserBehaviorLog logEntity = new UserBehaviorLog();
        logEntity.setUserId(message.getUserId());
        logEntity.setTargetType(UserBehaviorLog.TargetType.valueOf(message.getTargetType()));
        logEntity.setTargetId(message.getTargetId());
        logEntity.setBehaviorType(UserBehaviorLog.BehaviorType.valueOf(message.getBehaviorType()));
        logEntity.setTimestamp(message.getTimestamp());

        userBehaviorLogRepository.save(logEntity);
        log.info("用户行为已持久化入库：{}", logEntity);
    }

}
