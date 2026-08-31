package be.kdg.prog6.order.port.out;

import be.kdg.prog6.common.domain.DishId;
import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.order.domain.DishProjection;

import java.util.List;
import java.util.Optional;

public interface LoadDishProjectionPort {
    List<DishProjection> loadByRestaurantId(RestaurantId restaurantId);
    Optional<DishProjection> loadById(DishId dishId);
}