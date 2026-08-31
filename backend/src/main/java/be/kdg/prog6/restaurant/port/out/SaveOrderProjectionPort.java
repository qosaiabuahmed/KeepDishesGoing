package be.kdg.prog6.restaurant.port.out;

import be.kdg.prog6.restaurant.domain.OrderProjection;

public interface SaveOrderProjectionPort {
    void save(OrderProjection orderProjection);
}