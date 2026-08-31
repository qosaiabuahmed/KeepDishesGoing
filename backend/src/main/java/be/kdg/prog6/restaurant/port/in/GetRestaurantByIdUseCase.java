package be.kdg.prog6.restaurant.port.in;

import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.restaurant.domain.Restaurant;
import java.util.Optional;

public interface GetRestaurantByIdUseCase {
    Optional<Restaurant> getRestaurantById(RestaurantId restaurantId);
}