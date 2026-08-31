package be.kdg.prog6.common.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQSetup {

    public static final String MAIN_EXCHANGE = "kdg.events";
    public static final String QUEUE_ORDER_ACCEPTED = "queue.order.accepted.v1";
    public static final String QUEUE_ORDER_READY = "queue.order.ready.v1";
    public static final String QUEUE_ORDER_PICKED = "queue.order.picked.v1";
    public static final String QUEUE_ORDER_DELIVERED = "queue.order.delivered.v1";
    public static final String QUEUE_ORDER_LOCATION = "queue.order.location.v1";

    @Bean
    public TopicExchange mainExchange() {
        return ExchangeBuilder.topicExchange(MAIN_EXCHANGE)
                .durable(true)
                .build();
    }

    @Bean
    public Queue orderAcceptedQueue() {
        return QueueBuilder.durable(QUEUE_ORDER_ACCEPTED).build();
    }

    @Bean
    public Queue orderReadyQueue() {
        return QueueBuilder.durable(QUEUE_ORDER_READY).build();
    }

    @Bean
    public Queue orderPickedQueue() {
        return QueueBuilder.durable(QUEUE_ORDER_PICKED).build();
    }

    @Bean
    public Queue orderDeliveredQueue() {
        return QueueBuilder.durable(QUEUE_ORDER_DELIVERED).build();
    }

    @Bean
    public Queue orderLocationQueue() {
        return QueueBuilder.durable(QUEUE_ORDER_LOCATION).build();
    }

    @Bean
    public Binding acceptedOrderBinding(TopicExchange mainExchange) {
        return BindingBuilder.bind(orderAcceptedQueue())
                .to(mainExchange)
                .with("restaurant.*.order.accepted.v1");
    }

    @Bean
    public Binding readyOrderBinding(TopicExchange mainExchange) {
        return BindingBuilder.bind(orderReadyQueue())
                .to(mainExchange)
                .with("restaurant.*.order.ready.v1");
    }

    @Bean
    public Binding pickedOrderBinding(TopicExchange mainExchange) {
        return BindingBuilder.bind(orderPickedQueue())
                .to(mainExchange)
                .with("delivery.*.order.pickedup.v1");
    }

    @Bean
    public Binding deliveredOrderBinding(TopicExchange mainExchange) {
        return BindingBuilder.bind(orderDeliveredQueue())
                .to(mainExchange)
                .with("delivery.*.order.delivered.v1");
    }

    @Bean
    public Binding locationOrderBinding(TopicExchange mainExchange) {
        return BindingBuilder.bind(orderLocationQueue())
                .to(mainExchange)
                .with("delivery.*.order.location.v1");
    }
}