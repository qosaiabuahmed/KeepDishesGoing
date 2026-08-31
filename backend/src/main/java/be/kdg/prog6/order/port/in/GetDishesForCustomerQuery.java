package be.kdg.prog6.order.port.in;

import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.order.domain.DishProjection;

import java.util.List;

public interface GetDishesForCustomerQuery {
    List<DishProjection> getAvailableDishes(RestaurantId restaurantId);
}