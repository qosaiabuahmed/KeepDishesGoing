package be.kdg.prog6.restaurant.port.out;

import be.kdg.prog6.common.domain.OrderId;
import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.restaurant.domain.OrderProjection;
import be.kdg.prog6.restaurant.domain.OrderProjectionStatus;

import java.util.List;
import java.util.Optional;

public interface LoadOrderProjectionPort {
    Optional<OrderProjection> loadById(OrderId orderId);
    List<OrderProjection> loadByRestaurantId(RestaurantId restaurantId);
    List<OrderProjection> loadByRestaurantIdAndStatus(RestaurantId restaurantId, OrderProjectionStatus status);
}