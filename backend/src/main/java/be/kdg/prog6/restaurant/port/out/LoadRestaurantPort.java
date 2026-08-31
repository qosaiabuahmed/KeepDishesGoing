package be.kdg.prog6.restaurant.port.out;

import be.kdg.prog6.restaurant.domain.OwnerId;
import be.kdg.prog6.restaurant.domain.Restaurant;
import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.restaurant.domain.RestaurantName;

import java.util.List;
import java.util.Optional;

public interface LoadRestaurantPort {
    Optional<Restaurant> loadByName(RestaurantName name);
    Optional<Restaurant> loadById(RestaurantId id);
    Optional<Restaurant> loadByOwnerId(OwnerId ownerId);
    List<Restaurant> loadAll();
}