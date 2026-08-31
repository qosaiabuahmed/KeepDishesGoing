package be.kdg.prog6.order.port.out;

import be.kdg.prog6.order.domain.Order;

public interface SaveOrderPort {
    Order save(Order order);
}