package be.kdg.prog6.restaurant.adapter.in.messaging;

import be.kdg.prog6.common.domain.OrderId;
import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.common.events.OrderAcceptedEvent;
import be.kdg.prog6.common.events.OrderReadyForPickupEvent;
import be.kdg.prog6.common.events.OrderRejectedEvent;
import be.kdg.prog6.common.events.OrderSubmittedEvent;
import be.kdg.prog6.restaurant.domain.OrderProjection;
import be.kdg.prog6.restaurant.domain.OrderProjectionStatus;
import be.kdg.prog6.restaurant.port.out.LoadOrderProjectionPort;
import be.kdg.prog6.restaurant.port.out.SaveOrderProjectionPort;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderEventListener {

    private final List<SaveOrderProjectionPort> saveOrderProjectionPorts;
    private final LoadOrderProjectionPort loadOrderProjectionPort;

    public OrderEventListener(List<SaveOrderProjectionPort> saveOrderProjectionPorts, LoadOrderProjectionPort loadOrderProjectionPort) {
        this.saveOrderProjectionPorts = saveOrderProjectionPorts;
        this.loadOrderProjectionPort = loadOrderProjectionPort;
    }

    @EventListener
    @Transactional
    public void on(OrderSubmittedEvent event) {
        var items = event.items().stream()
                .map(item -> new OrderProjection.OrderItemProjection(
                        item.dishId(),
                        item.dishName(),
                        item.unitPrice(),
                        item.quantity(),
                        item.totalPrice()
                ))
                .collect(Collectors.toList());

        OrderProjection projection = new OrderProjection(
                event.orderId(),
                event.restaurantId(),
                event.customerId(),
                event.customerName(),
                event.contactEmail(),
                event.deliveryAddress().street(),
                event.deliveryAddress().number(),
                event.deliveryAddress().city(),
                event.deliveryAddress().postalCode(),
                event.deliveryAddress().country(),
                items,
                event.totalAmount(),
                event.paymentTransactionId(),
                event.orderTime(),
                OrderProjectionStatus.SUBMITTED
        );

        saveOrderProjectionPorts.forEach(port -> port.save(projection));
    }

    @EventListener
    @Transactional
    public void on(OrderAcceptedEvent event) {
        OrderProjection projection = loadOrderProjectionPort.loadById(new OrderId(event.orderId()))
                .orElseThrow(() -> new IllegalStateException("Order projection not found: " + event.orderId()));

        projection.setStatus(OrderProjectionStatus.CONFIRMED);
        projection.setDecisionTime(event.occurredAt());

        saveOrderProjectionPorts.forEach(port -> port.save(projection));
    }

    @EventListener
    @Transactional
    public void on(OrderRejectedEvent event) {
        OrderProjection projection = loadOrderProjectionPort.loadById(event.orderId())
                .orElseThrow(() -> new IllegalStateException("Order projection not found: " + event.orderId()));

        projection.setStatus(OrderProjectionStatus.CANCELLED);
        projection.setRejectionReason(event.rejectionReason());
        projection.setDecisionTime(event.occurredAt());

        saveOrderProjectionPorts.forEach(port -> port.save(projection));
    }

    @EventListener
    @Transactional
    public void on(OrderReadyForPickupEvent event) {
        OrderProjection projection = loadOrderProjectionPort.loadById(new OrderId(event.orderId()))
                .orElseThrow(() -> new IllegalStateException("Order projection not found: " + event.orderId()));

        projection.setStatus(OrderProjectionStatus.READY);

        saveOrderProjectionPorts.forEach(port -> port.save(projection));
    }
}