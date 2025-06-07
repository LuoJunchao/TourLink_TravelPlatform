package org.tourlink.socialservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // 队列名称
    public static final String BEHAVIOR_QUEUE = "behavior.queue";

    // 交换机名称
    public static final String BEHAVIOR_EXCHANGE = "behavior.exchange";

    // 路由键
    public static final String BEHAVIOR_ROUTING_KEY = "behavior.route";

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

    // 4. RabbitTemplate 用于发送消息
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }
}
