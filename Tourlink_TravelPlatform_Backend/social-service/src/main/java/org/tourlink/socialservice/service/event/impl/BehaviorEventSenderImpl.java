package org.tourlink.socialservice.service.event.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.tourlink.common.dto.dataPlatformDTO.UserBehaviorMessage;
import org.tourlink.socialservice.config.RabbitMQConfig;
import org.tourlink.socialservice.service.event.BehaviorEventSender;

@Component
@RequiredArgsConstructor
public class BehaviorEventSenderImpl implements BehaviorEventSender {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void send(UserBehaviorMessage message) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.BEHAVIOR_EXCHANGE,
                RabbitMQConfig.BEHAVIOR_ROUTING_KEY,
                message
        );
    }
}
