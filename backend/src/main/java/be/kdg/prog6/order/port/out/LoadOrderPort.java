package be.kdg.prog6.order.port.out;

import be.kdg.prog6.common.domain.OrderId;
import be.kdg.prog6.order.domain.Order;
import be.kdg.prog6.order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LoadOrderPort {
    Optional<Order> loadById(OrderId orderId);
    List<Order> findByStatusAndOrderTimeBefore(OrderStatus status, LocalDateTime time);
}