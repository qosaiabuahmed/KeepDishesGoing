package be.kdg.prog6.restaurant.port.in;

import be.kdg.prog6.common.domain.RestaurantId;

public interface CreateRestaurantUseCase {
    RestaurantId createRestaurant(CreateRestaurantCommand command);
}