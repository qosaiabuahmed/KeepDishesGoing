package be.kdg.prog6.restaurant.port.out;

import be.kdg.prog6.common.domain.RestaurantId;
import be.kdg.prog6.restaurant.domain.Menu;
import be.kdg.prog6.restaurant.domain.MenuId;

import java.util.Optional;

public interface LoadMenuPort {
    Optional<Menu> loadById(MenuId menuId);
    Optional<Menu> loadByRestaurantId(RestaurantId restaurantId);
}