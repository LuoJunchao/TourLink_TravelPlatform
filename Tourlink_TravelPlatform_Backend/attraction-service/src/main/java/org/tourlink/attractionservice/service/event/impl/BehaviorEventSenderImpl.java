package org.tourlink.attractionservice.service.event.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.tourlink.attractionservice.config.RabbitMQConfig;
import org.tourlink.attractionservice.service.event.BehaviorEventSender;
import org.tourlink.common.dto.dataPlatformDTO.UserBehaviorMessage;

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
