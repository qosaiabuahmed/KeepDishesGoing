package be.kdg.prog6.order.port.out;

import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.order.domain.RestaurantProjection;

import java.util.List;
import java.util.Optional;

public interface LoadRestaurantProjectionPort {
    List<RestaurantProjection> loadAll();
    Optional<RestaurantProjection> loadById(RestaurantId restaurantId);
}