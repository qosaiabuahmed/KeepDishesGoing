package be.kdg.prog6.order.port.in;

import be.kdg.prog6.order.domain.Order;

import java.util.Optional;

public interface GetOrderQuery {
    Optional<Order> getOrder(GetOrderQueryRequest query);
}