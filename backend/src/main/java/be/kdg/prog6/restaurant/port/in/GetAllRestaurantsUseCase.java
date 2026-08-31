package be.kdg.prog6.restaurant.port.in;

import be.kdg.prog6.restaurant.domain.Restaurant;
import java.util.List;

public interface GetAllRestaurantsUseCase {
    List<Restaurant> getAllRestaurants();
}