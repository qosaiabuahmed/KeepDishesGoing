package be.kdg.prog6.order.port.in;

import be.kdg.prog6.order.domain.RestaurantProjection;

import java.util.List;

public interface GetRestaurantsForCustomerQuery {
    List<RestaurantProjection> getAllRestaurants();
}