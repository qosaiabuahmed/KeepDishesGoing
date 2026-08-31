package be.kdg.prog6.order.adapter.in.messaging;

import be.kdg.prog6.common.config.RabbitMQSetup;
import be.kdg.prog6.common.domain.OrderId;
import be.kdg.prog6.common.events.external.*;
import be.kdg.prog6.order.domain.Order;
import be.kdg.prog6.order.domain.OrderStatus;
import be.kdg.prog6.order.port.out.LoadOrderPort;
import be.kdg.prog6.order.port.out.SaveOrderPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.amqp.support.AmqpHeaders.CONSUMER_QUEUE;
import static org.springframework.amqp.support.AmqpHeaders.RECEIVED_ROUTING_KEY;

@Component
public class DeliveryServiceEventListener {

    private static final Logger log = LoggerFactory.getLogger(DeliveryServiceEventListener.class);

    private final LoadOrderPort loadOrderPort;
    private final List<SaveOrderPort> saveOrderPort;

    public DeliveryServiceEventListener(
            LoadOrderPort loadOrderPort,
            List<SaveOrderPort> saveOrderPort) {
        this.loadOrderPort = loadOrderPort;
        this.saveOrderPort = saveOrderPort;
    }

    @RabbitListener(queues = RabbitMQSetup.QUEUE_ORDER_PICKED)
    @Transactional
    public void onOrderPickedUp(
            OrderPickedUpExternalEvent event,
            @Header(CONSUMER_QUEUE) String queue,
            @Header(RECEIVED_ROUTING_KEY) String routingKey) {

        log.info("[{}] rk='{}' orderId={}", queue, routingKey, event.orderId());

        OrderId orderId = new OrderId(event.orderId());
        Order order = loadOrderPort.loadById(orderId).orElse(null);

        if (order == null) {
            log.warn("Received OrderPickedUp for unknown order: {}. Ignoring event.", event.orderId());
            return;
        }

        order.updateStatus(OrderStatus.IN_DELIVERY);
        saveOrderPort.forEach(port -> port.save(order));

        log.info("Order {} status updated to IN_DELIVERY", event.orderId());
    }

    @RabbitListener(queues = RabbitMQSetup.QUEUE_ORDER_LOCATION)
    @Transactional
    public void onOrderLocation(
            OrderLocationExternalEvent event,
            @Header(CONSUMER_QUEUE) String queue,
            @Header(RECEIVED_ROUTING_KEY) String routingKey) {

        if (event == null || event.lat() == null || event.lng() == null) {
            log.warn("Received null event or location");
            return;
        }

        log.info("[{}] rk='{}' orderId={} location=({}, {})",
                queue, routingKey, event.orderId(),
                event.lat(), event.lng());

        OrderId orderId = new OrderId(event.orderId());
        Order order = loadOrderPort.loadById(orderId).orElse(null);

        if (order == null) {
            log.warn("Received location update for unknown order: {}. Ignoring event.", event.orderId());
            return;
        }

        order.updateCourierLocation(event.lat(), event.lng());
        order.updateStatus(OrderStatus.IN_DELIVERY);
        saveOrderPort.forEach(port -> port.save(order));

        log.debug("Courier location updated for order {}: lat={}, lng={}",
                event.orderId(), event.lat(), event.lng());
    }

    @RabbitListener(queues = RabbitMQSetup.QUEUE_ORDER_DELIVERED)
    @Transactional
    public void onOrderDelivered(
            OrderDeliveredExternalEvent event,
            @Header(CONSUMER_QUEUE) String queue,
            @Header(RECEIVED_ROUTING_KEY) String routingKey) {

        log.info("[{}] rk='{}' orderId={}", queue, routingKey, event.orderId());

        OrderId orderId = new OrderId(event.orderId());
        Order order = loadOrderPort.loadById(orderId).orElse(null);

        if (order == null) {
            log.warn("Received OrderDelivered for unknown order: {}. Ignoring event.", event.orderId());
            return;
        }

        order.updateStatus(OrderStatus.DELIVERED);
        saveOrderPort.forEach(port -> port.save(order));

        log.info("Order {} status updated to DELIVERED", event.orderId());
    }
}