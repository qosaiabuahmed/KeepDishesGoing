package be.kdg.prog6.order.port.out;

import be.kdg.prog6.order.domain.RestaurantProjection;

public interface SaveRestaurantProjectionPort {
    void save(RestaurantProjection projection);
}