package org.tourlink.dataplatformservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String BEHAVIOR_QUEUE = "behavior.queue";

    @Bean
    public Queue behaviorQueue() {
        return new Queue(BEHAVIOR_QUEUE);
    }
}
