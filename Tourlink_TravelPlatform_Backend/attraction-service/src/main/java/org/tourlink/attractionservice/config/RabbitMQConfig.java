package org.tourlink.attractionservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tourlink.common.constant.RabbitConstants;

@Configuration
public class RabbitMQConfig implements RabbitConstants {

    // 1. 声明一个直连交换机（Direct Exchange）
    @Bean
    public DirectExchange behaviorExchange() {
        return new DirectExchange(BEHAVIOR_EXCHANGE);
    }

    // 2. 声明一个队列
    @Bean
    public Queue behaviorQueue() {
        return new Queue(BEHAVIOR_QUEUE, true);
    }

    // 3. 将队列和交换机通过路由键绑定
    @Bean
    public Binding behaviorBinding(Queue behaviorQueue, DirectExchange behaviorExchange) {
        return BindingBuilder.bind(behaviorQueue).to(behaviorExchange).with(BEHAVIOR_ROUTING_KEY);
    }

}
